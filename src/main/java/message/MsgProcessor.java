package message;

import java.io.IOException;
import java.util.*;


public class MsgProcessor {

    private List<Sale> sales = new ArrayList<>();
    private List<Adjustment> adjustments = new ArrayList<>();
    /**
     * map the product type to a list that contains
     * total number of sales on position 0
     * and total value on position 1
     */
    private Map<String, List<Integer>> salesPerProduct =  new HashMap<>();


    public static final String CURRENCY = "p";
    public static final int SALE_REPORT_INTERVAL = 10;
    public static final int ADJUSTMENTS_REPORT_INTERVAL = 50;

    /**
     * entry point
     * @param type
     * @param message
     */
    public void processMessage(MsgType type, String message){
        switch(type){
            case SINGLE: addSingle(message);
            case MULTIPLE: addMultiple(message);
            case ADJUSTMENT: makeAdjustment(message);
        }
    }

    private void addSingle(String message){
        //TODO: validate
        Sale sale = createSingle(message);
        addSale(sale);
    }

    private void addMultiple(String message){
        //TODO: validate
        Sale sale = createMultiple(message);
        addSale(sale);
    }

    private void makeAdjustment(String message){
        //TODO: validate
        String[] tokens = message.split(" ");
        String productType = tokens[2];
        Integer adjustmentValue = Integer.parseInt(tokens[1].replaceFirst(CURRENCY, ""));
        OperationType opType = OperationType.valueOf(tokens[0]);

        Adjustment adjustment = new Adjustment(opType, productType, adjustmentValue);
        adjustSales(adjustment);
        adjustments.add(adjustment);
    }

    void adjustSales(Adjustment adjustment){
        sales.stream()
                //get sales that have the required product type
                .filter(sale -> sale.getProductType().equals(adjustment.getProductType()))
                //iterate through the sales and apply the required adjustment
                .forEach(sale -> {
                    Integer newValue = applyOperation(sale.getPrice(), adjustment.getOperationType(), adjustment.getModifier());
                    sale.setPrice(newValue);
                });
    }

    private Integer applyOperation(Integer value, OperationType operation, Integer modifier){
        switch(operation) {
            case ADD:
                return value + modifier;
            case MULTIPLY:
                return value * modifier;
            case SUBSTRACT:
                return value - modifier;
            default:
                //should never end here
                return value;
        }
    }

    Sale createSingle(String message){
        //assume last word is the price
        Integer price = Integer.parseInt(message.substring(message.lastIndexOf(" ") + 1, message.lastIndexOf(CURRENCY)));
        //product type should be the first word
        String productType = message.substring(0, message.indexOf(" "));

        Sale sale = new Sale(productType, price);
        return sale;
    }

    Sale createMultiple(String message){
        //split the message into words
        String[] tokens = message.split(" ");
        //product type should be the 4th word
        String productType = tokens[3];
        //need to remove the currency before parsing the integer for the price
        Integer price = Integer.parseInt(tokens[5].replaceFirst(CURRENCY, ""));
        //occurences should be the first word
        Integer occurences = Integer.parseInt(tokens[0]);

        Sale sale = new Sale(productType, price, occurences);
        return sale;
    }

    void addSale(Sale sale){
        sales.add(sale);
        updateSalesPerProd(sale);
        printReports();
    }

    void updateSalesPerProd(Sale sale){
        String prodType = sale.getProductType();

        //product type was never recorded before
        if(salesPerProduct.get(prodType) == null){
            List<Integer> values = new ArrayList<>();
            values.add(0, sale.getOccurences());
            values.add(1, sale.getPrice());
            salesPerProduct.put(prodType, values);
        }

        //update the total number of sales per product
        Integer currentNbrOfSales = salesPerProduct.get(prodType).get(0);
        Integer updatedNbrOfSales = currentNbrOfSales + sale.getOccurences();
        salesPerProduct.get(prodType).set(0, updatedNbrOfSales);

        //update the total value per product
        Integer currentTotalValue = salesPerProduct.get(prodType).get(1);
        Integer updatedTotalValue = currentTotalValue + sale.getPrice();
        salesPerProduct.get(prodType).set(1, updatedTotalValue);
    }

    private void printReports(){
        //check if we need to print the sales report
        Integer numberOfMessages = sales.size();
        if(numberOfMessages % SALE_REPORT_INTERVAL == 0){
            reportSalesSummary();
        }
        //check if we need to print the adjustments report
        if(numberOfMessages >= ADJUSTMENTS_REPORT_INTERVAL){
            reportAdjustments();
        }
    }

    void reportSalesSummary(){
        salesPerProduct.entrySet()
                .stream()
                .forEach(entry -> {
                        System.out.println(entry.getKey() + " has made ");
                        System.out.println(entry.getValue().get(0) + " sales");
                        System.out.println("with a total value of " + entry.getValue().get(1));
                });
    }

    void reportAdjustments(){
        System.out.println("Application is pausing, will not accept new messages");
        adjustments.stream()
                .forEach(adjustment ->
                        System.out.println(adjustment.toString())
                );
        System.out.println("Resume ?");
        //enter key will resume the application
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * package-protected getters and setters used only for testing
     * @param sales
     */

    void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    List<Sale> getSales(){
        return sales;
    }

    void setAdjustments(List<Adjustment> adjustments) {
        this.adjustments = adjustments;
    }

    void setSalesPerProduct(Map<String, List<Integer>> salesPerProduct) {
        this.salesPerProduct = salesPerProduct;
    }

    Map<String, List<Integer>> getSalesPerProduct(){
        return salesPerProduct;
    }

}

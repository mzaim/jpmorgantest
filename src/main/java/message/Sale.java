package message;

public class Sale {

    private String productType;
    private Integer occurences;
    private Integer price;

    public Sale(String productType, Integer price){
        occurences = 1;
        setProductType(productType);
        setPrice(price);
    }

    public Sale(String productType, Integer price, Integer occurences){
        setProductType(productType);
        setPrice(price);
        setOccurences(occurences);
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getOccurences() {
        return occurences;
    }

    public void setOccurences(Integer occurences) {
        this.occurences = occurences;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

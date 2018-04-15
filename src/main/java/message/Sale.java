package message;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(productType, sale.productType) &&
                Objects.equals(occurences, sale.occurences) &&
                Objects.equals(price, sale.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productType, occurences, price);
    }
}

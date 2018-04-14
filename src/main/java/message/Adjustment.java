package message;

public class Adjustment {

    private OperationType operationType;
    private String productType;
    private Integer modifier;

    public Adjustment(OperationType operationType, String productType, Integer modifier) {
        this.operationType = operationType;
        this.productType = productType;
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "Adjustment{" +
                "operationType=" + operationType +
                ", productType='" + productType + '\'' +
                ", modifier=" + modifier +
                '}';
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }
}

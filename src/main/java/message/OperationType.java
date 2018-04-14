package message;

public enum OperationType {
    ADD("Add"),
    SUBSTRACT("Substract"),
    MULTIPLY("Multiply");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

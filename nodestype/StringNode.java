package nodestype;

public class StringNode extends ASTNode {
    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String evaluate() {
        return value;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "String: " + value);
    }

}
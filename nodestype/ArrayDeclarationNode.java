package nodestype;

public class ArrayDeclarationNode extends ASTNode {
    private String varName;
    private int arraySize;

    public ArrayDeclarationNode(String varName, int arraySize) {
        this.varName = varName;
        this.arraySize = arraySize;
    }

    @Override
    public String evaluate() {
        return "int " + varName + "[" + arraySize + "];";
    }
}

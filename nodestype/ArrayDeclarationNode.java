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

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "ArrayDeclaration: " + varName);
    }
    
}

package nodestype;

public class ArrayAccessNode extends ASTNode {
    private String identifier;
    private ASTNode index;

    public ArrayAccessNode(String identifier, ASTNode index) {
        this.identifier = identifier;
        this.index = index;
    }

    @Override
    public String evaluate() {
        return identifier + "[" + index.evaluate().toString() + "]";
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "ArrayAccess:");
        System.out.println(indent + "  " + identifier);
        index.printTree(indent + "  ");
    }
    
}
package nodestype;


public class NumberNode extends ASTNode {
    private int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public String evaluate() {
        return Integer.toString(this.value);
    }

    @Override
    public String toString() {
        return "Number: " + this.value;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Number: " + this.value);
    }

    
}
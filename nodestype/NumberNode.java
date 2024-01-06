package nodestype;


public class NumberNode extends ASTNode {
    int value;

    public NumberNode(int value) {
        this.value = value;
    }

    public int getNumber() {
        return this.value;
    }

    @Override
    public String evaluate() {
        return Integer.toString(this.value);
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public void printTree(String indent) {
        System.out.println(this.value);
    }

    
}
package nodestype;

public class AndNode extends ASTNode {
    private ASTNode left;
    private ASTNode right;

    public AndNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    public ASTNode getLeft() {
        return this.left;
    }

    public ASTNode getRight() {
        return this.right;
    }

    @Override
    public String evaluate() {
        return this.left.evaluate() + " && " + this.right.evaluate();
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "&&");
        this.left.printTree(indent + "  ");
        this.right.printTree(indent + "  ");
    }
}

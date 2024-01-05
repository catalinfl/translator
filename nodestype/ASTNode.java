package nodestype;

public abstract class ASTNode {
    public abstract String evaluate();

    public void printTree(String indent) {
        System.out.println(indent + this.toString());
    }
}

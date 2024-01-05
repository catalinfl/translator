package nodestype;

public class PrintNode extends ASTNode {
    private ASTNode expression;

    public PrintNode(ASTNode expression) {
        this.expression = expression;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public String evaluate() {
        return "print " + expression.evaluate();
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Print:");
        expression.printTree(indent + "  ");
    }
}
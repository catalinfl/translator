package nodestype;

public class AssignmentNode extends ASTNode {
    private String identifier;
    private ASTNode expression;

    public AssignmentNode(String identifier, ASTNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getVarName() {
        return this.identifier;
    }

    public ASTNode getExpression() {
        return this.expression;
    }

    @Override
    public String evaluate() {
        return this.identifier + " = " + this.expression.evaluate() + ";";
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Assignment:");
        System.out.println(indent + "  " + this.identifier);
        expression.printTree(indent + "  ");
    }
    

}
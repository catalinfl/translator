package nodestype;

public class AssignmentNode extends ASTNode {
    private String identifier;
    private ASTNode expression;

    public AssignmentNode(String identifier, ASTNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String evaluate() {
        return this.identifier + " = " + this.expression.evaluate() + ";";
    }

}
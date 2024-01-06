package nodestype;

public class IfNode extends ASTNode {
    private ASTNode condition;
    private ASTNode body;

    public IfNode(ASTNode condition, ASTNode body) {
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return this.condition;
    }

    public ASTNode getBody() {
        return this.body;
    }

    @Override
    public String evaluate() {
        if (Integer.parseInt(condition.evaluate()) != 0) {
            return body.evaluate();
        } else {
            return "";
        }
    }
}
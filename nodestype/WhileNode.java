package nodestype;

public class WhileNode extends ASTNode {
    private ASTNode condition;
    private ASTNode body;

    public WhileNode(ASTNode condition, ASTNode body) {
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
        StringBuilder result = new StringBuilder();
        while (Integer.parseInt(condition.evaluate()) != 0) {
            result.append(body.evaluate());
        }
        return result.toString();
    }
}
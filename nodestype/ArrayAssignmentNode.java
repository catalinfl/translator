package nodestype;

public class ArrayAssignmentNode extends ASTNode {
    private String varName;
    private ASTNode arrayIndex;
    private ASTNode expression;

    public ArrayAssignmentNode(String varName, ASTNode arrayIndex, ASTNode expression) {
        this.varName = varName;
        this.arrayIndex = arrayIndex;
        this.expression = expression;
    }
    
    public String getVarName() {
        return this.varName;
    }

    public ASTNode getArrayIndex() {
        return this.arrayIndex;
    }

    public ASTNode getExpression() {
        return this.expression;
    }

    public ASTNode getType() {
        return this.expression;
    }

    @Override
    public String evaluate() {
        String varName = this.varName;
        String index = this.arrayIndex.evaluate();
        String expression = this.expression.evaluate();
        return varName + "[" + index + "] = " + expression;
    }
}
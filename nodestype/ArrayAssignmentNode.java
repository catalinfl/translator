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

    public ASTNode getType() {
        return this.expression;
    }

    @Override
    public String evaluate() {
        return this.varName + "[" + this.arrayIndex.evaluate() + "]" + " = " + this.expression.evaluate() + ";";
    }
}
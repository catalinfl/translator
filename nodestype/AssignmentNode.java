package nodestype;

public class AssignmentNode extends ASTNode {
    private String variableName;
    private int value;

    public AssignmentNode(String variableName, int value) {
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String evaluate() {
        return this.variableName + " = " + this.value + ";";
    }
    
}
package nodestype;

public class VariableDeclarationNode extends ASTNode {
    private String identifier;
    private Integer arraySize;

    public VariableDeclarationNode(String identifier, Integer arraySize) {
        this.identifier = identifier;
        this.arraySize = arraySize;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Integer getArraySize() {
        return arraySize;
    }

    // get Expression   

    public ASTNode getExpression() {
        return this;
    }


    @Override
    public String evaluate() {
        if (arraySize == null) {
            return "int " + identifier + ";";
        } else {
            return "int " + identifier + "[" + arraySize + "];";
        }
    }
}
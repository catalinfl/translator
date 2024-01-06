package nodestype;

public class VariableDeclarationNode extends ASTNode {
    private String identifier;
    private Integer arraySize;

    public VariableDeclarationNode(String identifier, Integer arraySize) {
        this.identifier = identifier;
        this.arraySize = arraySize;
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
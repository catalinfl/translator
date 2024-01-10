package nodestype;

public class IdentifierNode extends ASTNode {
    private String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ASTNode getExpression() {
        return this;
    }

    @Override
    public String evaluate() {
        return this.name;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Identifier: " + name);
    }

}

package nodestype;

public class ReadNode extends ASTNode {
    private ASTNode varName;

    public ReadNode(ASTNode varName) {
        this.varName = varName;
    }

    public String getVarName() {
        return this.evaluate();
    }

    public ASTNode getExpression() {
        return this.varName;
    }

    @Override
    public String evaluate() {
        return "cin >> " + this.varName;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Read:");
        System.out.println(indent + "  " + this.varName);
    }
}

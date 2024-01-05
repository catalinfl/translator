package nodestype;

public class OperatorNode extends ASTNode {
    private String operator;
    private ASTNode left;
    private ASTNode right;

    public OperatorNode(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    @Override
    public String evaluate() {
        String leftCode = this.left.evaluate();
        String rightCode = this.right.evaluate();
        
        switch(this.operator) {
            case "+":
                return leftCode + " + " + rightCode;
            case "-":
                return leftCode + " - " + rightCode;
            case "*":
                return leftCode + " * " + rightCode;
            case "/":
                return  leftCode + " / " + rightCode;
            default:
                throw new RuntimeException("Unknown operator: " + this.operator);
        }
    }

    @Override
    public String toString() {
    return "Operator: " + this.operator;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Operator: " + this.operator);
        System.out.println(indent + "Left operand:");
        this.left.printTree(indent + "  ");
        System.out.println(indent + "Right operand:");
        this.right.printTree(indent + "  ");
    }
}

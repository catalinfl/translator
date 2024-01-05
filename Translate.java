import nodestype.ASTNode;
import nodestype.NumberNode;
import nodestype.OperatorNode;
import nodestype.PrintNode;
import nodestype.ProgramNode;
import nodestype.StringNode;

public class Translate {

    
    public String toCCode(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("using namespace std; \n");
        sb.append("int main() { \n");
        if (node instanceof ProgramNode) {
                for (ASTNode statement : ((ProgramNode) node).getStatements()) {
                    sb.append(toCCode(statement)).append("\n");
                }
                return sb.toString();
            }
        else if (node instanceof NumberNode) {
            return ((NumberNode) node).evaluate();
        } else if (node instanceof OperatorNode) {
            OperatorNode opNode = (OperatorNode) node;
            String leftCode = toCCode(opNode.getLeft());
            String rightCode = toCCode(opNode.getRight());
            if (opNode.getOperator().equals("*") || opNode.getOperator().equals("/")) {
                return addParenthesesIfNeeded(leftCode) + " " + opNode.getOperator() + " " + rightCode;
            } else {
                return leftCode + " " + opNode.getOperator() + " " + rightCode;
            }
        } else if (node instanceof PrintNode) {
        PrintNode printNode = (PrintNode) node;
        return "cout << " + "\"" + toCCode(printNode.getExpression()) + "\"" + " << endl;";
        }
        else if (node instanceof StringNode) {
            return ((StringNode) node).evaluate();
        }
        else {
            throw new IllegalArgumentException("Unsupported node type: " + node.getClass().getName());
        }
    }

    private static String addParenthesesIfNeeded(String code) {
        if (!isSurroundedByParentheses(code)) {
            return "(" + code + ")";
        } else {
            return code;
        }
    }

    private static boolean isSurroundedByParentheses(String code) {
        return code.startsWith("(") && code.endsWith(")");
    }
}
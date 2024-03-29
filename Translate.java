import java.util.HashSet;
import java.util.Set;

import nodestype.ASTNode;
import nodestype.AndNode;
import nodestype.ArrayAssignmentNode;
import nodestype.ArrayDeclarationNode;
import nodestype.AssignmentNode;
import nodestype.BlockNode;
import nodestype.IdentifierNode;
import nodestype.IfNode;
import nodestype.NumberNode;
import nodestype.OperatorNode;
import nodestype.PrintNode;
import nodestype.ProgramNode;
import nodestype.ReadNode;
import nodestype.StringNode;
import nodestype.VariableDeclarationNode;
import nodestype.WhileNode;
import nodestype.ArrayAccessNode;


public class Translate {


    public String toCCode(ASTNode node, int indentLevel) {
        Set<String> declaredVariables = new HashSet<>();
        StringBuilder sb = new StringBuilder();


    
        if (node instanceof ProgramNode) {
            sb.append("#include <iostream> \n");
            sb.append("using namespace std; \n");
            sb.append("int main() { \n");
            for (ASTNode statement : ((ProgramNode) node).getStatements()) {
                sb.append(toCCode(statement, indentLevel + 1)).append("\n");
            }
            return sb.toString();
        } else if (node instanceof NumberNode) {
            return ((NumberNode) node).evaluate();
        } else if (node instanceof OperatorNode) {
            OperatorNode opNode = (OperatorNode) node;
            String leftCode = toCCode(opNode.getLeft(), indentLevel);
            String rightCode = toCCode(opNode.getRight(), indentLevel);
            if (opNode.getOperator().equals("*") || opNode.getOperator().equals("/")) {
                return addParenthesesIfNeeded(leftCode) + " " + opNode.getOperator() + " " + rightCode;
            } else {
                return leftCode + " " + opNode.getOperator() + " " + rightCode;
            }
        } else if (node instanceof ReadNode) {
            ReadNode readNode = (ReadNode) node;
            ASTNode expression = readNode.getExpression();
            String identifier;
            if (expression instanceof IdentifierNode) {
                identifier = ((IdentifierNode)expression).getName();
            } else if (expression instanceof ArrayAccessNode) {
                ArrayAccessNode arrayAccess = (ArrayAccessNode)expression;
                identifier = arrayAccess.evaluate();

            } else {
                throw new IllegalArgumentException("Unsupported expression type in ReadNode: " + expression.getClass().getName());
            }
            return indent(indentLevel) + "cin >> " + identifier + ";";
        }
        else if (node instanceof PrintNode) {
            PrintNode printNode = (PrintNode) node;
            if (printNode.getExpression() instanceof ArrayAccessNode) {
                return indent(indentLevel) + "cout << " + printNode.getExpression().evaluate() + ";";
            }
            if (printNode.getExpression() instanceof IdentifierNode) {
                return indent(indentLevel) + "cout << " + printNode.getExpression().evaluate() + ";";
            }
            if (printNode.getExpression() instanceof NumberNode) {
                return indent(indentLevel) + "cout << " + printNode.getExpression().evaluate() + ";";
            } 
            if (printNode.getExpression().evaluate().contains("\\n")) {
                return indent(indentLevel) + "cout << " + "\""  + printNode.getExpression().evaluate().substring(0, printNode.getExpression().evaluate().length() - 3) + "\""  + " << endl;";
            }
            return indent(indentLevel) + "cout << " + "\"" + toCCode(printNode.getExpression(), indentLevel) + "\"" + ";";
        } else if (node instanceof AndNode) {
            AndNode andNode = (AndNode) node;
            String leftCode = toCCode(andNode.getLeft(), indentLevel);
            String rightCode = toCCode(andNode.getRight(), indentLevel);
            return leftCode + " && " + rightCode;
        }
        
        else if (node instanceof StringNode) {
            return ((StringNode) node).evaluate();
        } else if (node instanceof VariableDeclarationNode) {
            String variableName = ((VariableDeclarationNode) node).getIdentifier();
            declaredVariables.add(variableName);
            return indent(indentLevel) + ((VariableDeclarationNode) node).evaluate();
        } else if (node instanceof AssignmentNode) {
            AssignmentNode assignmentNode = (AssignmentNode) node;
            String varName = assignmentNode.evaluate().split("=")[0].trim();
            String expression = assignmentNode.evaluate().split("=")[1].trim();
            if (expression.contains("0") && expression.contains("-")) {
                expression = "-"+expression.split("-")[1].trim();
            }
            return indent(indentLevel) + varName + " = " + expression;
        } else if (node instanceof IdentifierNode) {
            return ((IdentifierNode) node).evaluate();
        } else if (node instanceof ArrayDeclarationNode) {
            return indent(indentLevel) + ((ArrayDeclarationNode) node).evaluate();
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            String conditionCode = toCCode(ifNode.getCondition(), indentLevel);
            String bodyCode = toCCode(ifNode.getBody(), indentLevel + 1);
            return indent(indentLevel) + "if (" + conditionCode + ") {\n" + bodyCode + indent(indentLevel) + "}";
        } else if (node instanceof WhileNode) {
            WhileNode whileNode = (WhileNode) node;
            String conditionCode = toCCode(whileNode.getCondition(), indentLevel);
            String bodyCode = toCCode(whileNode.getBody(), indentLevel + 1);
            return indent(indentLevel) + "while (" + conditionCode + ") {\n" + bodyCode + indent(indentLevel) + "}";
        }
        else if (node instanceof BlockNode) {
            BlockNode blockNode = (BlockNode) node;
            StringBuilder blockCode = new StringBuilder();
            for (ASTNode statement : blockNode.getStatements()) {
                blockCode.append(toCCode(statement, indentLevel + 1)).append("\n");
            }
            return blockCode.toString();
        } else if (node instanceof ArrayAccessNode) {
            return ((ArrayAccessNode) node).evaluate();
        } else if (node instanceof ArrayAssignmentNode) {
            ArrayAssignmentNode arrayAssignmentNode = (ArrayAssignmentNode) node;
            String varName = arrayAssignmentNode.evaluate().split("\\[")[0];
            String index = arrayAssignmentNode.evaluate().split("\\[")[1].split("\\]")[0];
            String expression = arrayAssignmentNode.evaluate().split("=")[1].trim();
            return indent(indentLevel) + varName + "[" + index + "] = " + expression + ";";
        } 
        else if (node instanceof AssignmentNode) {
            AssignmentNode assignmentNode = (AssignmentNode) node;
            String varName = assignmentNode.evaluate().split("=")[0].trim();
            String expression = assignmentNode.evaluate().split("=")[1].trim();
            return indent(indentLevel) + varName + " = " + expression + ";";
        }
        else if (node instanceof IdentifierNode) {
            return ((IdentifierNode) node).evaluate();
        }
        else {
            throw new IllegalArgumentException("Unsupported node type: " + node.getClass().getName());
        }
    }

    private String indent (int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
        return sb.toString();
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

import java.util.ArrayList;

import nodestype.ASTNode;
import nodestype.AssignmentNode;
import nodestype.NumberNode;
import nodestype.OperatorNode;
import nodestype.PrintNode;
import nodestype.ProgramNode;
import nodestype.StringNode;
import nodestype.VariableDeclarationNode;

public class Parser extends ASTNode {

    private ArrayList<Token> tokens;
    private ArrayList<String> declaredVariables = new ArrayList<>();
    private int pos = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        ProgramNode program = new ProgramNode();
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            if (token.getType() == Token.Type.PRINT) {
                program.addStatement(printStatement());
            } else if (token.getType() == Token.Type.VAR) {
                program.addStatement(variableDeclaration());
            }
                else if (token.getType() == Token.Type.IDENTIFIER && 
                tokens.get(pos + 1).getType() == Token.Type.EQUALS) {
                program.addStatement(assignment());
            } 
            
            else {
                program.addStatement(expression());
            }
        }
        return program;
    }

    private ASTNode expression() {
        ASTNode node = term();
    
        while (pos < tokens.size()) {
            if (tokens.get(pos).getType() == Token.Type.PLUS) {
                consume(Token.Type.PLUS);
                ASTNode right = term();
                node = new OperatorNode("+", node, right);
            } else if (tokens.get(pos).getType() == Token.Type.MINUS) {
                consume(Token.Type.MINUS);
                ASTNode right = term();
                node = new OperatorNode("-", node, right);
            } else {
                break;
            }
        }
    
        return node;
    }
    
    private ASTNode term() {
        ASTNode node = factor();
    
        while (pos < tokens.size()) {
            if (tokens.get(pos).getType() == Token.Type.MULTIPLY) {
                consume(Token.Type.MULTIPLY);
                ASTNode right = factor();
                node = new OperatorNode("*", node, right);
            } else if (tokens.get(pos).getType() == Token.Type.DIVIDE) {
                consume(Token.Type.DIVIDE);
                ASTNode right = factor();
                node = new OperatorNode("/", node, right);
            } else {
                break;
            }
        }
    
        return node;
    }

    private ASTNode factor() {
        if (pos >= tokens.size()) {
            throw new RuntimeException("Unexpected end of input");
        }
    
        Token token = tokens.get(pos++);
        ASTNode node;
    
        switch (token.getType()) {
            case NUMBER:
                node = new NumberNode(Integer.parseInt(token.getValue()));
                break;
            case PARANTHESIS_OPEN:
                node = expression();
                if (tokens.get(pos++).getType() != Token.Type.PARANTHESIS_CLOSE) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                break;
            case PRINT:
                node = printStatement();
                break;
            case STRING:
                node = new StringNode(token.getValue());
                break;
            case EOF:
                node = new StringNode("}");
                break;
            default:
                throw new RuntimeException("Unexpected token: " + token.getValue());
        }
    
        return node;
    }

    private ASTNode printStatement() {
        consume(Token.Type.PRINT);
        if (pos >= tokens.size() || tokens.get(pos).getType() != Token.Type.STRING) {
            throw new RuntimeException("Expected string after print");
        }
        Token token = tokens.get(pos++);
        return new PrintNode(new StringNode(token.getValue()));
    }

    private ASTNode variableDeclaration() {
        consume(Token.Type.VAR);
        if (pos >= tokens.size() || tokens.get(pos).getType() != Token.Type.IDENTIFIER) {
            throw new RuntimeException("Expected identifier after var");
        }
        Token identifierToken = tokens.get(pos++);
        if (declaredVariables.contains(identifierToken.getValue())) {
            throw new RuntimeException("Variable " + identifierToken.getValue() + " is already declared");
        }
        Integer arraySize = null;
        if (pos < tokens.size() && tokens.get(pos).getType() == Token.Type.BRACKET_OPEN) {
            consume(Token.Type.BRACKET_OPEN);
            if (pos >= tokens.size() || tokens.get(pos).getType() != Token.Type.NUMBER) {
                throw new RuntimeException("Expected number after [");
            }
            Token numberToken = tokens.get(pos++);
            arraySize = Integer.parseInt(numberToken.getValue());
            consume(Token.Type.BRACKET_CLOSE);
        }
        declaredVariables.add(identifierToken.getValue());
        return new VariableDeclarationNode(identifierToken.getValue(), arraySize);
    }

    private ASTNode assignment() {
        Token identifierToken = tokens.get(pos++);
        consume(Token.Type.EQUALS);
        if (pos >= tokens.size() || tokens.get(pos).getType() != Token.Type.NUMBER) {
            throw new RuntimeException("Expected number after =");
        }
        Token numberToken = tokens.get(pos++);
        
        Integer value = Integer.parseInt(numberToken.getValue());
        return new AssignmentNode(identifierToken.getValue(), value);
    }

    public void consume(Token.Type type) {
        if (pos < tokens.size() && tokens.get(pos).getType() == type) {
            pos++;
        } else {
            throw new RuntimeException("Unexpected token: " + tokens.get(pos).getValue());
        }
    }

    @Override
    public String evaluate() {
        return this.parse().evaluate();
    }
}
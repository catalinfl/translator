import java.util.ArrayList;

import nodestype.ASTNode;
import nodestype.NumberNode;
import nodestype.OperatorNode;
import nodestype.PrintNode;
import nodestype.ProgramNode;
import nodestype.StringNode;

public class Parser extends ASTNode {
    private ArrayList<Token> tokens;
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
            } else {
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

    if (token.getType() == Token.Type.NUMBER) {
        return new NumberNode(Integer.parseInt(token.getValue()));
    } else if (token.getType() == Token.Type.PARANTHESIS_OPEN) {
        ASTNode node = expression();
        if (tokens.get(pos++).getType() != Token.Type.PARANTHESIS_CLOSE) {
            throw new RuntimeException("Mismatched parentheses");
        }
        return node;
    } else if (token.getType() == Token.Type.PRINT) {
        return printStatement();
    } else if (token.getType() == Token.Type.STRING) {
        return new StringNode(token.getValue());
    } else if (token.getType() == Token.Type.EOF) {
        return new StringNode("}"); 
    }
     else {
        throw new RuntimeException("Unexpected token: " + token.getValue());
    }
    }

    private ASTNode printStatement() {
        consume(Token.Type.PRINT);
        if (pos >= tokens.size() || tokens.get(pos).getType() != Token.Type.STRING) {
            throw new RuntimeException("Expected string after print");
        }
        Token token = tokens.get(pos++);
        return new PrintNode(new StringNode(token.getValue()));
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
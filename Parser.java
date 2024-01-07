import java.util.ArrayList;

import nodestype.ASTNode;
import nodestype.ArrayAccessNode;
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

public class Parser extends ASTNode {

    private ArrayList<Token> tokens;
    public ArrayList<String> declaredVariables = new ArrayList<>();
    private int pos = 0;

    public ArrayList<String> getDeclaredVariables() {
        return declaredVariables;
    }

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        ProgramNode program = new ProgramNode();
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            if (token.getType() == Token.Type.PRINT) {
                program.addStatement(printStatement());
            }
            if (token.getType() == Token.Type.READ) {
                program.addStatement(readStatement());
            }
            else if (token.getType() == Token.Type.VAR) {
                program.addStatement(variableDeclaration());
            } else if (token.getType() == Token.Type.IF) {
                program.addStatement(ifStatement());
            } else if (token.getType() == Token.Type.WHILE) {
                program.addStatement(whileStatement());
            } else if (token.getType() == Token.Type.IDENTIFIER) {
                if (!declaredVariables.contains(token.getValue())) {
                    throw new RuntimeException("Variable " + token.getValue() + " is not declared");
                }
                if (tokens.get(pos + 1).getType() == Token.Type.EQUALS) {
                    program.addStatement(assignment());
                } else {
                    program.addStatement(expression());
                }
            } else {
                program.addStatement(expression());
            }
        }
        return program;
    }

    private ASTNode readStatement() {
        consume(Token.Type.READ);
        if (pos >= tokens.size() || 
            (tokens.get(pos).getType() != Token.Type.STRING && 
             tokens.get(pos).getType() != Token.Type.IDENTIFIER && 
             tokens.get(pos).getType() != Token.Type.NUMBER)) {
            throw new RuntimeException("Expected string, identifier, or number after read");
        }
        if (tokens.get(pos).getType() == Token.Type.STRING) {
            Token token = tokens.get(pos++);
            return new ReadNode(new StringNode(token.getValue()));
        }
        if (tokens.get(pos).getType() == Token.Type.NUMBER) {
            Token token = tokens.get(pos++);
            return new ReadNode(new NumberNode(Integer.parseInt(token.getValue())));
        }
        if (tokens.get(pos).getType() == Token.Type.IDENTIFIER) {
            Token token = tokens.get(pos++);
            ASTNode index = null;
            if (pos < tokens.size() && tokens.get(pos).getType() == Token.Type.BRACKET_OPEN) {
                consume(Token.Type.BRACKET_OPEN);
                index = expression();
                if (tokens.get(pos++).getType() != Token.Type.BRACKET_CLOSE) {
                    throw new RuntimeException("Expected ']' after array index");
                }
            }
            if (index != null) {
                return new ReadNode(new ArrayAccessNode(token.getValue(), index));
            } else {
                return new ReadNode(new IdentifierNode(token.getValue()));
            }
        }
        throw new RuntimeException("Unexpected token read: " + tokens.get(pos).getValue() + " " + tokens.get(pos).getLine());
    }




    
    private ASTNode ifStatement() {
        consume(Token.Type.IF);
        ASTNode condition = expression();
        BlockNode body = new BlockNode();
        Token token = tokens.get(pos);
        while (token.getType() != Token.Type.END) {
            if (token.getType() == Token.Type.PRINT) {
                body.addStatement(printStatement());
            } else if (token.getType() == Token.Type.VAR) {
                body.addStatement(variableDeclaration());
            } else if (token.getType() == Token.Type.IF) {
                body.addStatement(ifStatement());
            } else if (token.getType() == Token.Type.WHILE) {
                body.addStatement(whileStatement());
            } else if (token.getType() == Token.Type.IDENTIFIER) {
                if (!declaredVariables.contains(token.getValue())) {
                    throw new RuntimeException("Variable " + token.getValue() + " is not declared");
                }
                if (tokens.get(pos + 1).getType() == Token.Type.EQUALS) {
                    body.addStatement(assignment());
                } else {
                    body.addStatement(expression());
                }
            } else if (token.getType() == Token.Type.READ) {
                body.addStatement(readStatement());
            }
            else {
                body.addStatement(expression());
            }
            token = tokens.get(pos);
        }
        consume(Token.Type.END);  // Consume the 'end' token
        return new IfNode(condition, body);
    }

    private ASTNode whileStatement() {
        consume(Token.Type.WHILE);
        ASTNode condition = expression();
        BlockNode body = new BlockNode();
        Token token = tokens.get(pos);
        while (token.getType() != Token.Type.END) {
            if (token.getType() == Token.Type.PRINT) {
                body.addStatement(printStatement());
            } else if (token.getType() == Token.Type.READ) {
                body.addStatement(readStatement());
            }
            else if (token.getType() == Token.Type.VAR) {
                body.addStatement(variableDeclaration());
            } else if (token.getType() == Token.Type.IF) {
                body.addStatement(ifStatement());
            } else if (token.getType() == Token.Type.WHILE) {
                body.addStatement(whileStatement());
            } else if (token.getType() == Token.Type.IDENTIFIER) {
                if (!declaredVariables.contains(token.getValue())) {
                    throw new RuntimeException("Variable " + token.getValue() + " is not declared");
                }
                if (tokens.get(pos + 1).getType() == Token.Type.EQUALS) {
                    body.addStatement(assignment());
                } else {
                    body.addStatement(expression());
                }
            } else {
                body.addStatement(expression());
            }
            token = tokens.get(pos);
        }
        consume(Token.Type.END);  // Consume the 'end' token
        return new WhileNode(condition, body);
    }

    
    

    private ASTNode expression() {
        ASTNode node = term();
        
        
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            if (token.getType() == Token.Type.PLUS) {
                consume(Token.Type.PLUS);
                ASTNode right = term();
                node = new OperatorNode("+", node, right);
            } else if (token.getType() == Token.Type.MINUS) {
                consume(Token.Type.MINUS);
                ASTNode right = term();
                node = new OperatorNode("-", node, right);
            } else if (token.getType() == Token.Type.GREATER_THAN) {
                consume(Token.Type.GREATER_THAN);
                ASTNode right = term();
                node = new OperatorNode(">", node, right);
            } else if (token.getType() == Token.Type.SMALLER_THAN) {
                consume(Token.Type.SMALLER_THAN);
                ASTNode right = term();
                node = new OperatorNode("<", node, right);
            } else if (token.getType() == Token.Type.EQUAL) {
                consume(Token.Type.EQUAL);
                ASTNode right = term();
                node = new OperatorNode("==", node, right);
            }            
            else {
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
                case IDENTIFIER:
                String varName = token.getValue();
                ASTNode index = null;
                if (pos < tokens.size() && tokens.get(pos).getType() == Token.Type.BRACKET_OPEN) {
                    consume(Token.Type.BRACKET_OPEN);
                    index = expression();
                    if (tokens.get(pos++).getType() != Token.Type.BRACKET_CLOSE) {
                        throw new RuntimeException("Expected ']' after array index");
                    }
                }
                if (pos < tokens.size() && tokens.get(pos).getType() == Token.Type.EQUALS) {
                    consume(Token.Type.EQUALS);
                    ASTNode expression = expression();
                    if (index != null) {
                        node = new ArrayAssignmentNode(varName, index, expression);
                    } else {
                        node = new AssignmentNode(varName, expression);
                    }
                } else {
                    if (index != null) {
                        node = new ArrayAccessNode(varName, index);
                    } else {
                        node = new IdentifierNode(varName);
                    }
                }
                break;
            case PLUS:
                node = new OperatorNode("+", null, null);
                break;
            case MINUS:
                node = new OperatorNode("-", null, null);
                break;
            case MULTIPLY:
                node = new OperatorNode("*", null, null);
                break; 
            case DIVIDE:
                node = new OperatorNode("/", null, null);
                break;
            case EQUAL:
                node = new OperatorNode("==", null, null);
                break;
            case BRACKET_OPEN:
                node = new StringNode("[");
                break;
            case BRACKET_CLOSE:
                node = new StringNode("]");
                break;
            case EQUALS:
                node = new StringNode("=");
                break;
            case READ:
                node = readStatement();
                break;
            default:
                throw new RuntimeException("Unexpected token:" + token.getValue() + " " + token.getLine());
        }
    
        return node;
    }

    private ASTNode printStatement() {
        consume(Token.Type.PRINT);
        if (pos >= tokens.size() || 
            (tokens.get(pos).getType() != Token.Type.STRING && 
             tokens.get(pos).getType() != Token.Type.IDENTIFIER && 
             tokens.get(pos).getType() != Token.Type.NUMBER)) {
            throw new RuntimeException("Expected string, identifier, or number after print");
        }
        if (tokens.get(pos).getType() == Token.Type.STRING) {
            Token token = tokens.get(pos++);
            return new PrintNode(new StringNode(token.getValue()));
        }
        if (tokens.get(pos).getType() == Token.Type.NUMBER) {
            Token token = tokens.get(pos++);
            return new PrintNode(new NumberNode(Integer.parseInt(token.getValue())));
        }
        if (tokens.get(pos).getType() == Token.Type.IDENTIFIER) {
            Token token = tokens.get(pos++);
            ASTNode index = null;
            if (pos < tokens.size() && tokens.get(pos).getType() == Token.Type.BRACKET_OPEN) {
                consume(Token.Type.BRACKET_OPEN);
                index = expression();
                if (tokens.get(pos++).getType() != Token.Type.BRACKET_CLOSE) {
                    throw new RuntimeException("Expected ']' after array index");
                }
            }
            if (index != null) {
                return new PrintNode(new ArrayAccessNode(token.getValue(), index));
            } else {
                return new PrintNode(new IdentifierNode(token.getValue()));
            }
        }
        throw new RuntimeException("Unexpected token print: " + tokens.get(pos).getValue() + " " + tokens.get(pos).getLine());
    }


    private ASTNode variableDeclaration() {
    Token token = tokens.get(pos++);
    if (token.getType() != Token.Type.VAR) {
            throw new RuntimeException("Expected 'var'");
    }
        token = tokens.get(pos++);
    if (token.getType() != Token.Type.IDENTIFIER) {
        throw new RuntimeException("Expected identifier after 'var'");
    }
    String varName = token.getValue();
    if (declaredVariables.contains(varName)) {
        throw new RuntimeException("Variable " + varName + " is already declared");
    }
        declaredVariables.add(varName);
    token = tokens.get(pos++);
    if (token.getType() == Token.Type.BRACKET_OPEN) {
        token = tokens.get(pos++);
        if (token.getType() != Token.Type.NUMBER) {
            throw new RuntimeException("Expected array size");
        }
        int arraySize = Integer.parseInt(token.getValue());
        token = tokens.get(pos++);
        if (token.getType() != Token.Type.BRACKET_CLOSE) {
            throw new RuntimeException("Expected ']' after array size");
        }
        return new ArrayDeclarationNode(varName, arraySize);
    } else {
        pos--; // Unread the token
        return new VariableDeclarationNode(varName, null);
    }
}


    
private ASTNode assignment() {
    Token token = tokens.get(pos++);
    if (token.getType() != Token.Type.IDENTIFIER) {
        throw new RuntimeException("Expected identifier");
    }
    String varName = token.getValue();
    ASTNode indexNode = null;
    if (tokens.get(pos).getType() == Token.Type.BRACKET_OPEN) {
        consume(Token.Type.BRACKET_OPEN);
        indexNode = expression();
        consume(Token.Type.BRACKET_CLOSE);
    }
    if (!declaredVariables.contains(varName)) {
        throw new RuntimeException("Variable " + varName + " is not declared");
    }
    token = tokens.get(pos++);
    if (token.getType() != Token.Type.EQUALS) {
        throw new RuntimeException("Expected '='");
    }
    ASTNode expression = expression();
    if (indexNode != null) {
        return new ArrayAssignmentNode(varName, indexNode, expression);
    } else {
        return new AssignmentNode(varName, expression);
    }
}


    public void consume(Token.Type type) {
        if (pos < tokens.size() && tokens.get(pos).getType() == type) {
            pos++;
        } 
        if (pos >= tokens.size()) {
            throw new RuntimeException("Unexpected end of input");
        }
    }

    @Override
    public String evaluate() {
        return this.parse().evaluate();
    }
}
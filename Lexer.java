import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;

public class Lexer {

    private String filename;
    private ArrayList<Token> tokens = new ArrayList<Token>();


    public Lexer(String filename) {
        this.filename = filename;
    }

    public void Tokenize() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tokens.addAll(tokenizeLine(line));
            }
            tokens.add(new Token(Token.Type.EOF, "EOF", -1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Token> tokenizeLine(String line) {
        ArrayList<Token> lineTokens = new ArrayList<>();
        try {
            int c;
            int lineNo = 1;
            PushbackReader pushbackReader = new PushbackReader(new StringReader(line));
            while ((c = pushbackReader.read()) != -1) {
                char character = (char) c;
                if (character == ' ') {
                    continue;
                }
                if (character == '#') {
                    while ((c = pushbackReader.read()) != -1) {
                        character = (char) c;
                        if (character == '\n') {
                            lineNo++;
                            break;
                        }
                    }
                    continue;
                }
                if (character == '\n') {
                    lineNo++;
                    continue;
                }
                else if (Character.isLetter(character)) {
                    String word = String.valueOf(character);
                    while (Character.isLetterOrDigit((char) (c = pushbackReader.read()))) {
                        word += String.valueOf((char) c);
                    }
                    pushbackReader.unread(c);
                    if (word.equals("print")) {
                        tokens.add(new Token(Token.Type.PRINT, word, lineNo));
                        while (Character.isWhitespace((char) (c = pushbackReader.read()))) {
                            // do nothing
                        }
                        pushbackReader.unread(c);
                    } else if (word.equals("var")) {
                        tokens.add(new Token(Token.Type.VAR, word, lineNo));
                    } else if (word.equals("read")) {
                        tokens.add(new Token(Token.Type.READ, word, lineNo));
                    } else if (word.equals("while")) {
                        tokens.add(new Token(Token.Type.WHILE, word, lineNo));
                    } else if (word.equals("if")) {
                        tokens.add(new Token(Token.Type.IF, word, lineNo));
                    } else if (word.equals("end")) {
                        tokens.add(new Token(Token.Type.END, word, lineNo));
                    }
                    else {
                        tokens.add(new Token(Token.Type.IDENTIFIER, word, lineNo));
                    }
                    } else if (character == '"') {
                    String stringLiteral = "";
                    while ((c = pushbackReader.read()) != -1 && (char) c != '"') {
                        stringLiteral += String.valueOf((char) c);
                    }
                    if (c == -1) {
                        throw new RuntimeException("Unterminated string literal");
                    }
                    tokens.add(new Token(Token.Type.STRING, stringLiteral, lineNo));
                }                
                // pentru numere
                else if (Character.isDigit(character)) {
                    String number = String.valueOf(character);
                    while (Character.isDigit((char) (c = pushbackReader.read()))) {
                        number += String.valueOf((char) c);
                    }
                    pushbackReader.unread(c);
                    tokens.add(new Token(Token.Type.NUMBER, number, lineNo));
                } else if (character == '+') {
                    tokens.add(new Token(Token.Type.PLUS, String.valueOf(character), lineNo));
                } else if (character == '-') {
                    tokens.add(new Token(Token.Type.MINUS, String.valueOf(character), lineNo));
                } else if (character == '*') {
                    tokens.add(new Token(Token.Type.MULTIPLY, String.valueOf(character), lineNo));
                } else if (character == '/') {
                    tokens.add(new Token(Token.Type.DIVIDE, String.valueOf(character), lineNo));
                } else if (character == '(') {
                    tokens.add(new Token(Token.Type.PARANTHESIS_OPEN, String.valueOf(character), lineNo));
                } else if (character == ')') {
                    tokens.add(new Token(Token.Type.PARANTHESIS_CLOSE, String.valueOf(character), lineNo));
                } else if (character == '&') {
                    tokens.add(new Token(Token.Type.AND, String.valueOf(character), lineNo));
                }
                 else if (character == '=') {
                    tokens.add(new Token(Token.Type.EQUALS, String.valueOf(character), lineNo));
                    String number = "";
                    while (Character.isWhitespace((char) (c = pushbackReader.read()))) {
                        // do nothing
                    }

                    while (Character.isDigit((char) c)) {
                        number += String.valueOf((char) c);
                        c = pushbackReader.read();
                    }
                    if (!number.isEmpty()) {
                        tokens.add(new Token(Token.Type.NUMBER, number, lineNo));
                    }
                    pushbackReader.unread(c);
                } else if (character == '<') {
                    tokens.add(new Token(Token.Type.SMALLER_THAN, String.valueOf(character), lineNo));
                } else if (character == '>') {
                    tokens.add(new Token(Token.Type.GREATER_THAN, String.valueOf(character), lineNo));
                    c = pushbackReader.read();
                    if (Character.isDigit((char) c)) {
                        String number = String.valueOf((char) c);
                        while (Character.isDigit((char) (c = pushbackReader.read()))) {
                            number += String.valueOf((char) c);
                        }
                        tokens.add(new Token(Token.Type.NUMBER, number, lineNo));
                    } else {
                        pushbackReader.unread(c);
                    }
                } else if (character == '[') {
                    tokens.add(new Token(Token.Type.BRACKET_OPEN, String.valueOf(character), lineNo));
                    String insideBrackets = "";
                    while ((char) (c = pushbackReader.read()) != ']') {
                        insideBrackets += String.valueOf((char) c);
                    }
                    ArrayList<Token> insideTokens = tokenizeLine(insideBrackets);
                    tokens.addAll(insideTokens);
                    tokens.add(new Token(Token.Type.BRACKET_CLOSE, "]", lineNo));
                    while (Character.isWhitespace((char) (c = pushbackReader.read()))) {
                        // do nothing
                    }
                    pushbackReader.unread(c);
                } else if (character == ']') {
                    tokens.add(new Token(Token.Type.BRACKET_CLOSE, String.valueOf(character), lineNo));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineTokens;
    }



    public ArrayList<Token> getTokens() {
        return tokens;
    }

  

}

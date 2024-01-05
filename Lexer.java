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
            tokens.add(new Token(Token.Type.EOF, "EOF", 0));
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
                }
                else if (character == '\r') {  
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
                    } else {
                        tokens.add(new Token(Token.Type.UNKNOWN, word, lineNo));
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
                    if (c == -1) {
                        tokens.add(new Token(Token.Type.NUMBER, number, lineNo));
                        break;
                    }
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
                } else {
                    tokens.add(new Token(Token.Type.UNKNOWN, String.valueOf(character), lineNo));
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

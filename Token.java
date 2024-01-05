public class Token {

    public enum Type {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        PARANTHESIS_OPEN,
        PARANTHESIS_CLOSE,
        EOF,
        PRINT,
        STRING,
        UNKNOWN
    }

    private Type type;
    private String value;
    private int line;

    public Token(Type type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public Type getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getLine() {
        return this.line;
    }
 }

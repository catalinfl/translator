import java.util.ArrayList;

import nodestype.ASTNode;

public class Main {
    
public static void main(String args[]) {
    if (args.length < 1) {
        System.out.println("Please provide a file name as a command-line argument");
        return;
    }

    String filename = args[0];
    Lexer lexer = new Lexer(filename);
    lexer.Tokenize();
    ArrayList<Token> tokens = lexer.getTokens();

    for (Token token : tokens) {
        System.out.println(token.getType() + " " + token.getValue());
    }
    try {
            Parser parser = new Parser(tokens);
            ASTNode root = parser.parse();
            root.printTree("");
            Translate translate = new Translate();
            String s = translate.toCCode(root, 0);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


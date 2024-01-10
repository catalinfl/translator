import java.util.ArrayList;

import nodestype.ASTNode;
import java.io.*;

public class Main {
    
public static void main(String args[]) {
    if (args.length < 1) {
        System.out.println("Provide program.txt as argument");
        return;
    }

    String filename = args[0];
    Lexer lexer = new Lexer(filename);
    lexer.Tokenize();
    ArrayList<Token> tokens = lexer.getTokens();

    FileWriter fw = null;
    try {
        fw = new FileWriter("tokens.txt");
        for (Token token : tokens) {
            fw.write(token.getType() + " " + token.getValue() + " " + token.getLine() + "\n");
        }
        fw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    Parser parser = new Parser(tokens);
    ASTNode root = parser.parse();

    // root.printTree(""); accesare AST

    Translate translate = new Translate();
    String s = translate.toCCode(root, 0);

    try {
        fw = new FileWriter("output.c");
        fw.write(s);
        fw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
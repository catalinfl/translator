import java.util.ArrayList;

import nodestype.ASTNode;
import java.io.*;

public class Main {
    
public static void main(String args[]) {
    if (args.length < 1) {
        System.out.println("Provide program.aoleu as argument");
        return;
    }

    String filename = "program/" + args[0]; // codul de interpretat trebuie sa fie in program/program.txt
    System.out.println("Watching " + filename);
    Lexer lexer = new Lexer(filename);
    lexer.Tokenize();
    ArrayList<Token> tokens = lexer.getTokens();

    FileWriter fw = null;
    try {
        fw = new FileWriter("outputs/tokens.txt");
        for (Token token : tokens) {
            fw.write(token.getType() + " " + token.getValue() + " " + token.getLine() + "\n");
        }
        fw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    System.out.println("Tokens generated in output/tokens.txt");

    Parser parser = new Parser(tokens);
    ASTNode root = parser.parse();

    // root.printTree(""); accesare AST

    Translate translate = new Translate();
    String s = translate.toCCode(root, 0);

    try {
        fw = new FileWriter("outputs/output.c");
        fw.write(s);
        fw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    System.out.println("C code generated in output/output.c");
}

}
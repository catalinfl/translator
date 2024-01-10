package nodestype;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> statements = new ArrayList<>();

    public void addStatement(ASTNode statement) {
        statements.add(statement);
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public String evaluate() {
        StringBuilder sb = new StringBuilder();
        for (ASTNode statement : statements) {
            sb.append(statement.evaluate()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "#include <iostream>";
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "Program:");
        for (ASTNode statement : statements) {
            statement.printTree(indent + "  ");
        }
    }
}
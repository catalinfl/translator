package nodestype;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends ASTNode {
    private List<ASTNode> statements;

    public BlockNode() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(ASTNode statement) {
        this.statements.add(statement);
    }

    public List<ASTNode> getStatements() {
        return this.statements;
    }

    @Override
    public String evaluate() {
        StringBuilder sb = new StringBuilder();
        for (ASTNode statement : this.statements) {
            sb.append(statement.evaluate()).append("\n");
        }
        return sb.toString();
    }
}

package parser;

import java.util.List;

public class BlockStmt implements Stmt {
    public final List<Stmt> stmts;

    public BlockStmt(final List<Stmt> stmts) {
        this.stmts = stmts;
    }
    public boolean equals(final Object o) {
        if (o instanceof BlockStmt) {
            final BlockStmt other = (BlockStmt) o;
            return stmts.equals(other.stmts);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return stmts.hashCode();
    }
    
    
}
    

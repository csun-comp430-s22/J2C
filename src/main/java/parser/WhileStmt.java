package parser;

public class WhileStmt implements Stmt {
    public final Exp guard;
    public final Stmt body;

    public WhileStmt(final Exp guard,
                     final Stmt body) {
        this.guard = guard;
        this.body = body;
    }

    public boolean equals(final Object other) {
        if (other instanceof WhileStmt) {
            final WhileStmt otherStmt = (WhileStmt)other;
            return (guard.equals(otherStmt.guard) &&
                    body.equals(otherStmt.body));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (guard.hashCode() +
                body.hashCode());
    }

    public String toString() {
        return ("WhileStmt(" +
                guard.toString() + ", " +
                body.toString() + ")");
    }
    
}

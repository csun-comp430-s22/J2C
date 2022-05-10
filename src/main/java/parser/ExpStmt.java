package parser;

public class ExpStmt implements Stmt {
    public final Exp exp;
    public ExpStmt(final Exp exp) {
        this.exp = exp;
    }

    public boolean equals(final Object o) {
        if (o instanceof ExpStmt) {
            final ExpStmt other = (ExpStmt) o;
            return exp.equals(other.exp);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return exp.hashCode();
    }
    public String toString() {
        return ("ExpStmt(" + exp.toString() + ")");
    }


}

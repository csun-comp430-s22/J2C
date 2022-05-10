package parser;

public class ReturnStmt implements Stmt {
    public final Exp exp;
    public ReturnStmt(final Exp exp) {
        this.exp = exp;
    }
    public boolean equals(final Object o) {
        if (o instanceof ReturnStmt) {
            final ReturnStmt other = (ReturnStmt) o;
            return exp.equals(other.exp);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return exp.hashCode();
    }
    public String toString() {
        return ("ReturnStmt(" + exp.toString() + ")");
    }
    
}

package parser;

public class VarAssignStmt implements Stmt {
    public final Exp id;
    public final Exp exp;
    public VarAssignStmt(final Exp id, final Exp exp) {
        this.id = id;
        this.exp = exp;
    }
    public boolean equals(final Object o) {
        if (o instanceof VarAssignStmt) {
            final VarAssignStmt other = (VarAssignStmt) o;
            return id.equals(other.id) && exp.equals(other.exp);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return id.hashCode() + exp.hashCode();
    }
    public String toString() {
        return ("VarAssignStmt(" + id.toString() + ", " + exp.toString() + ")");
    }
    
}

package parser;

public class ThisStmt implements Stmt {
    public final VariableExp id;
    public final VariableExp next;
    public ThisStmt(final VariableExp id, final VariableExp next) {
        this.id = id;
        this.next = next;
    }
    public boolean equals(final Object o) {
        if (o instanceof ThisStmt) {
            final ThisStmt other = (ThisStmt) o;
            return id.equals(other.id) && next.equals(other.next);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return id.hashCode() + next.hashCode();
    }
    public String toString() {
        return ("ThisStmt(" + id.toString() + ", " + next.toString() + ")");
    }
    
}

package parser;

public class BreakStmt implements Stmt {
   public final String label;
   public final String next;
    public BreakStmt(final String label, final String next) {
        this.label = label;
        this.next = next;
    }
    public boolean equals(final Object o) {
        if (o instanceof BreakStmt) {
            final BreakStmt other = (BreakStmt) o;
            return label.equals(other.label) && next.equals(other.next);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return label.hashCode() + next.hashCode();
    }
    public String toString() {
        return ("BreakStmt(" + label + ", " + next + ")");
    }

}

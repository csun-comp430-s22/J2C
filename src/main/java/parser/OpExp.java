package parser;

public class OpExp implements Exp {
    public final Exp left;
    public final Op op;
    public final Exp right;
    public OpExp(final Exp left, final Op op, final Exp right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public boolean equals(final Object o) {
        if (o instanceof OpExp) {
            final OpExp other = (OpExp) o;
            return left.equals(other.left) && op.equals(other.op) && right.equals(other.right);
        } else {
            return false;
        }
    }
    
    
    public int hashCode() {
        return left.hashCode() + op.hashCode() + right.hashCode();
    }

    public String toString() {
        return ("OpExp(" + left.toString() + ", " + op.toString() + ", " + right.toString() + ")");
    }
    
    
}

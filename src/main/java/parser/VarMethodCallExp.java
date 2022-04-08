package parser;

import java.util.List;

public class VarMethodCallExp implements Exp {

    public final Exp left;
    public final Exp methodName;
    public final List<Exp> args;

    public VarMethodCallExp(final Exp left, final Exp methodName, final List<Exp> args) {
        this.left = left;
        this.methodName = methodName;
        this.args = args;
    }

    public boolean equals(final Object o) {
        if (o instanceof VarMethodCallExp) {
            final VarMethodCallExp other = (VarMethodCallExp) o;
            return left.equals(other.left) && methodName.equals(other.methodName) && args.equals(other.args);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return left.hashCode() + methodName.hashCode() + args.hashCode();
    }
    public String toString() {
        return ("VarMethodCallExp(" + left.toString() + ", " + methodName.toString() + ", " + args.toString() + ")");
    }
    
    
}

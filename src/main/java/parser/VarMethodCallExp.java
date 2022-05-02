package parser;
import java.util.List;

public class VarMethodCallExp implements Exp {
    public final Exp var;
    public final MethodNameExp methodName;
    public final List<Exp> args;

    public VarMethodCallExp(final Exp var, final MethodNameExp methodName, final List<Exp> args) {
        this.var = var;
        this.methodName = methodName;
        this.args = args;
    }

    public boolean equals(final Object o) {
        if (o instanceof VarMethodCallExp) {
            final VarMethodCallExp other = (VarMethodCallExp) o;
            return var.equals(other.var) && methodName.equals(other.methodName) && args.equals(other.args);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return var.hashCode() + methodName.hashCode() + args.hashCode();
    }
    public String toString() {
        return ("VarMethodCallExp(" + var.toString() + ", " + methodName.toString() + ", " + args.toString() + ")");
    }
}

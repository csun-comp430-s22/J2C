package parser;

import java.util.List;

public class MethodCallExp implements Exp {

    public final Exp methodName;
    public final List<Exp> args;

    public MethodCallExp(final Exp methodName, final List<Exp> args) {
        this.methodName = methodName;
        this.args = args;
    }

    public boolean equals(final Object o) {
        if (o instanceof MethodCallExp) {
            final MethodCallExp other = (MethodCallExp) o;
            return methodName.equals(other.methodName) && args.equals(other.args);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return methodName.hashCode() + args.hashCode();
    }
    public String toString() {
        return "MethodCallExp(" + methodName.toString() + ", " + args.toString() + ")";
    }
}
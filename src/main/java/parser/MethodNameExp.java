package parser;

public class MethodNameExp implements Exp {
    public final MethodName methodName;
    public MethodNameExp(final MethodName methodName) {
        this.methodName = methodName;
    }
    public boolean equals(final Object o) {
        if (o instanceof MethodNameExp) {
            final MethodNameExp other = (MethodNameExp) o;
            return methodName.equals(other.methodName);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return methodName.hashCode();
    }
    public String toString() {
        return "MethodNameExp(" + methodName.toString() + ")";
    }
    
}

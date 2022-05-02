package parser;

public class ClassNameExp implements Exp {
    public final ClassName className;
    public ClassNameExp(final ClassName className) {
        this.className = className;
    }
    public boolean equals(final Object o) {
        if (o instanceof ClassNameExp) {
            final ClassNameExp other = (ClassNameExp) o;
            return className.equals(other.className);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return className.hashCode();
    }
    public String toString() {
        return ("ClassNameExp(" + className.toString() + ")");
    }

}
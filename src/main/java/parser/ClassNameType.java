package parser;

public class ClassNameType implements Type {
    public final ClassName className;
    public ClassNameType(final ClassName className) {
        this.className = className;
    }
    public boolean equals(final Object o) {
        if (o instanceof ClassNameType) {
            final ClassNameType other = (ClassNameType) o;
            return className.equals(other.className);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return className.hashCode();
    }
    public String toString() {
        return ("ClassNameType(" + className.toString() + ")");
    }
    
    
}

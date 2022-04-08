package parser;

public class ClassType implements Type {
    public final ClassName className;
    public ClassType(final ClassName className) {
        this.className = className;
    }
   
    public boolean equals(final Object other) {
        return (other instanceof ClassType) && className.equals(((ClassType) other).className);
    }
    public int hashCode() {
        return this.className.hashCode();
    }
    public String toString() {
        return "ClassType(" + this.className + ")";
    }
    
}

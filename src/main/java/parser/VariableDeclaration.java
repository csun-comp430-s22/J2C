package parser;

public class VariableDeclaration implements VarDec {
    public final Type type;
    public final Exp id;
    public final Exp init;
    public VariableDeclaration(final Type type, final Exp id, final Exp init) {
        this.type = type;
        this.id = id;
        this.init = init;
    }
    public boolean equals(final Object o) {
        if (o instanceof VariableDeclaration) {
            final VariableDeclaration other = (VariableDeclaration) o;
            return type.equals(other.type) && id.equals(other.id) && init.equals(other.init);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return type.hashCode() + id.hashCode() + init.hashCode();
    }
    public String toString() {
        return ("VariableDeclaration(" + type.toString() + ", " + id.toString() + ", " + init.toString() + ")");
    }
    
}

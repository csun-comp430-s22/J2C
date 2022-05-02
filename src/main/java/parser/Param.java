package parser;

public class Param {
    public final Type paramType;
    public final Exp var;
    public Param(final Type paramType, final Exp var) {
        this.paramType = paramType;
        this.var = var;
    }
    public boolean equals(final Object o) {
        if (o instanceof Param) {
            final Param other = (Param) o;
            return paramType.equals(other.paramType) && var.equals(other.var);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return paramType.hashCode() + var.hashCode();
    }
    public String toString() {
        return ("Param(" + paramType.toString() + ", " + var.toString() + ")");
    }


}

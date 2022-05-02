package parser;

public class SuperStmt implements Stmt {
    public final String name;
    public final Exp exp;

    public SuperStmt(final String name, final Exp exp) {
        this.name = name;
        this.exp = exp;
    }

    public boolean equals(final Object o) {
        if (o instanceof SuperStmt) {
            final SuperStmt other = (SuperStmt) o;
            return name.equals(other.name) && exp.equals(other.exp);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return name.hashCode() + exp.hashCode();
    }
    
}

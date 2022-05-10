package parser;

import java.util.List;

public class Program implements Node {
    public final List<ClassDef> classDefs;
    public final List<Stmt> stmts;

    public Program(final List<ClassDef> classDefs, final List<Stmt> stmts) {
        this.classDefs = classDefs;
        this.stmts = stmts;
    }

    public int hashCode() {
        return classDefs.hashCode() + stmts.hashCode();
    }

    public boolean equals(final Object o) {
        if (o instanceof Program) {
            final Program other = (Program) o;
            return classDefs.equals(other.classDefs) && stmts.equals(other.stmts);
        } else {
            return false;
        }
    }

    public String toString() {
        return ("Program(" + classDefs.toString() + ", " + stmts.toString() + ")");
    }
    
    
}

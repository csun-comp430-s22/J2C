package parser;

public class VarDecStmt implements Stmt {
    public final ParseResult<VarDec> varDec;

    public VarDecStmt(final ParseResult<VarDec> varDec) {
        this.varDec = varDec;
    }

    public boolean equals(final Object o) {
        if (o instanceof VarDecStmt) {
            final VarDecStmt other = (VarDecStmt) o;
            return varDec.equals(other.varDec);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return varDec.hashCode();
    }
    public String toString() {
        return ("VarDecStmt(" + varDec.toString() + ")");
    }



}

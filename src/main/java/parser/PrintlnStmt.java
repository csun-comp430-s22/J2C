package parser;

public class PrintlnStmt implements Stmt {
    public final Exp exp;

    public PrintlnStmt(final Exp exp) {
        this.exp = exp;
    }
    
    
}

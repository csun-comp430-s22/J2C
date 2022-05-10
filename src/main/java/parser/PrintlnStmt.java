package parser;
import java.util.List;

public class PrintlnStmt implements Stmt {
    public final List<Exp> exps;
    public PrintlnStmt(List<Exp> exps) {
        this.exps = exps;
    }

    public String toString() {
        return "println(" + exps.toString() + ")";
    }
    public boolean equals(Object o) {
        if (o instanceof PrintlnStmt) {
            PrintlnStmt other = (PrintlnStmt) o;
            return this.exps.equals(other.exps);
        }
        return false;
    }

    
}
package parser;

public class PlusOp implements Op {
    public int hashCode() {
        return 0;
    }
    public boolean equals(Object o) {
        return o instanceof PlusOp;
    }
    public String toString() {
        return "+";
    }
    


}

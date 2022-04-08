package parser;

public class FalseExp implements Exp {
    public boolean equals(final Object other) {
        return (other instanceof FalseExp);
    }
    public int hashCode() {
        return 272;
    }
    public String toString() {
        return "false";
    }
    

}

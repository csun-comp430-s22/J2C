package parser;

public class TrueExp implements Exp {
    public boolean equals(final Object other) {
        return (other instanceof TrueExp);
    }
    public int hashCode() {
        return 271;
    }
    public String toString() {
        return "true";
    }
    

}

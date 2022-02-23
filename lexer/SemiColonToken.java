package lexer;

public class SemiColonToken {
    public boolean equals(final Object other) {
        return other instanceof SemiColonToken;
    }
    public int hashCode() {
        return 7;
    }
    public String toString() {
        return ";";
    }
    
}

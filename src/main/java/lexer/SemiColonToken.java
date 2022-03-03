package lexer;

public class SemiColonToken implements Token {
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

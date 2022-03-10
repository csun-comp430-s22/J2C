package lexer;

public class EqualsToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof EqualsToken;
    }
    public int hashCode() {
        return 40;
    }
    
    public String toString() {
        return "==";
    }
    
}

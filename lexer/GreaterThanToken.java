package lexer;

public class GreaterThanToken {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanToken;
    }
    public int hashCode() {
        return 12;
    }
    public String toString() {
        return ">";
    }
    
}

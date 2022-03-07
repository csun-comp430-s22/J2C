package lexer;

public class NotToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof NotToken;
    }
    public int hashCode() {
        return 152;
    }
    public String toString() {
        return "!";
    }
    
    
}

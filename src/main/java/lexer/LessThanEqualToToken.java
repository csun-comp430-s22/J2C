package lexer;

public class LessThanEqualToToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LessThanEqualToToken;
    }
    public int hashCode() {
        return 78;
    }
    public String toString() {
        return "<=";
    }
    
}

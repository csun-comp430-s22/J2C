package lexer;

public class GreaterThanEqualToToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanEqualToToken;
    }
    public int hashCode() {
        return 77;
    }
    public String toString() {
        return ">=";
    }
}

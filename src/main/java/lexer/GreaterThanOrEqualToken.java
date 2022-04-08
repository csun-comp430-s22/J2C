package lexer;

public class GreaterThanOrEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanOrEqualToken;
    }
    public int hashCode() {
        return 80;
    }
    public String toString() {
        return ">=";
    }
}

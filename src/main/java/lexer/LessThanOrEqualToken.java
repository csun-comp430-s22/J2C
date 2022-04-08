package lexer;

public class LessThanOrEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LessThanOrEqualToken;
    }
    public int hashCode() {
        return 79;
    }
    public String toString() {
        return "<=";
    }
}

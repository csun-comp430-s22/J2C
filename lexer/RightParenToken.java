package lexer;

public class RightParenToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightParenToken;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return ")";
    }
}
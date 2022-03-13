package lexer;

public class BooleanToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof BooleanToken;
    }
    public int hashCode() {
        return 72;
    }
    public String toString() {
        return "Bool";
    }
}

package lexer;

public class BooleanTypeToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof BooleanTypeToken;
    }

    public int hashCode() {
        return 72;
    }

    public String toString() {
        return "bool";
    }
}

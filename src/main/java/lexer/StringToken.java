package lexer;

public class StringToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof StringToken;
    }

    public int hashCode() {
        return 73;
    }

    public String toString() {
        return "string";
    }

}
package lexer;

public class StrToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof StrToken;
    }

    public int hashCode() {
        return 73;
    }

    public String toString() {
        return "string";
    }

}
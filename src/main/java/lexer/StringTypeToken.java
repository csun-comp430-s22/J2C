package lexer;

public class StringTypeToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof StringTypeToken;
    }

    public int hashCode() {
        return 73;
    }

    public String toString() {
        return "string";
    }

}
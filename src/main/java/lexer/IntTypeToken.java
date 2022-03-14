package lexer;

public class IntTypeToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof IntTypeToken;
    }

    public int hashCode() {
        return 74;
    }

    public String toString() {
        return "int";
    }

}
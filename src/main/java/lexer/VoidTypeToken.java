package lexer;

public class VoidTypeToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof VoidTypeToken;
    }

    public int hashCode() {
        return 70;
    }

    public String toString() {
        return "void";
    }

}
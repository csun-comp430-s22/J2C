package lexer;

public class IntToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof IntToken;
    }

    public int hashCode() {
        return 74;
    }

    public String toString() {
        return "int";
    }

}
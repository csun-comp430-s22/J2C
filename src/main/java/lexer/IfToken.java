package lexer;

public class IfToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof IfToken;
    }
    public int hashCode() {
        return 1;
    }
    public String toString() {
        return "if";
    }

}
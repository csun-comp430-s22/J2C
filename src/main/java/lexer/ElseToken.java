package lexer;

public class ElseToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ElseToken;
    }
    public int hashCode() {
        return 2;
    }
    public String toString() {
        return "else";
    }
}

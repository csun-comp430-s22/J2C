package lexer;

public class PrintlnToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof PrintlnToken;
    }
    public int hashCode() {
        return 71;
    }
    public String toString() {
        return "Println";
    }
}

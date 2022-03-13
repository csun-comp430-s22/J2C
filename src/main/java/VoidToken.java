package lexer;

public class VoidToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof VoidToken;
    }
    public int hashCode() {
        return 70;
    }
    public String toString() {
        return "Void";
    }
    
}
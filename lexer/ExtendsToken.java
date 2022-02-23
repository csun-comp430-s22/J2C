package lexer;

public class ExtendsToken {
    public boolean equals(final Object other) {
        return other instanceof ExtendsToken;
    }
    public int hashCode() {
        return 11;
    }
    public String toString() {
        return "extends";
    }
    
}

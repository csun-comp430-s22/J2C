package lexer;

public class MultiplicationToken {
    public boolean equals(final Object other) {
        return other instanceof MultiplicationToken;
    }
    public int hashCode() {
        return 15;
    }
    public String toString() {
        return "*";
    }
    
    
}

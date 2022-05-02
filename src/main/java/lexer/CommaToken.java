package lexer;

public class CommaToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof CommaToken;
    }
    public int hashCode() {
        return 591;
    }
    public String toString() {
        return ",";
    }
    
}
    

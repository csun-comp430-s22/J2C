package lexer;

public class SubtractionToken {

    public boolean equals(final Object other) {
        return other instanceof SubtractionToken;
    }
    public int hashCode() {
        return 9;
    }
    public String toString() {
        return "-";
    }
    
}

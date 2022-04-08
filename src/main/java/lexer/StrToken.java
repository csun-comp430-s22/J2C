package lexer;


public class StrToken implements Token {
    
     final String value;

    public StrToken(final String value) {
        this.value = value;
    }
    public int hashCode() {
        return value.hashCode();
    }
    public String toString() {
        return "StrToken(" + value + ")";
    }
    public boolean equals(final Object other) {
        if (other instanceof StrToken) {
            final StrToken asVar = (StrToken)other;
            return value.equals(asVar.value);
        } else {
            return false;
        }
    }

}

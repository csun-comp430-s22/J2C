package lexer;


public class StringToken implements Token {
    
     public final String value;

    public StringToken(final String value) {
        this.value = value;
    }
    public int hashCode() {
        return value.hashCode();
    }
    public String toString() {
        return "StrToken(" + value + ")";
    }
    public boolean equals(final Object other) {
        if (other instanceof StringToken) {
            final StringToken asVar = (StringToken)other;
            return value.equals(asVar.value);
        } else {
            return false;
        }
    }

}

package parser;

public class StringLiteralExp implements Exp {
    public final String value;

    public StringLiteralExp(final String value) {
        this.value = value;
    }
    public boolean equals(final Object other) {
        return (other instanceof StringLiteralExp &&
                value.equals(((StringLiteralExp)other).value));
    }
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }

    
}

package parser;

public class StringExp implements Exp {
    public final String value;

    public StringExp(final String value) {
        this.value = value;
    }
    public boolean equals(final Object other) {
        return (other instanceof StringExp &&
                value.equals(((StringExp)other).value));
    }
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }

    
}

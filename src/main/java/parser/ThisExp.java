package parser;

public class ThisExp implements Exp {
    public final String value;

    public ThisExp(final String value) {
        this.value = value;
    }
    public boolean equals(final Object other) {
        return (other instanceof ThisExp &&
                value.equals(((ThisExp)other).value));
    }
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return "ThisExp(" + value.toString() + ")";
    }

    
}
package parser;

public class ClassName implements Exp {
    public final String value;
    public ClassName(final String value) {
        this.value = value;
    }
    public int hashCode() {
        return value.hashCode();
    }
    public boolean equals(final Object other) {
        return (other instanceof ClassName &&
                value.equals(((ClassName)other).value));
    }
    public String toString() {
        return "ClassName(" + value + ")";
    }
    
}

package parser;

public class IntType implements Type {
    public IntType() {
    }
    public boolean equals(final Object other) {
        return (other instanceof IntType);
    }
    public int hashCode() {
        return 240;
    }
    public String toString() {
        return "IntType()";
    }
    
}

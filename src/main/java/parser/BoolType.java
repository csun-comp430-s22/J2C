package parser;

public class BoolType implements Type {
    public BoolType() {
    }
    public boolean equals(final Object other) {
        return (other instanceof BoolType);
    }
    public int hashCode() {
        return 244;
    }
    public String toString() {
        return "BoolType()";
    }
    
    
}

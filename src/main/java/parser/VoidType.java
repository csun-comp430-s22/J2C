package parser;

public class VoidType implements Type {
    public boolean equals(final Object other) {
        return other instanceof VoidType;
    }

    public int hashCode() {
        return 449;
    }

    public String toString() {
        return "VoidType";
    }
}
    

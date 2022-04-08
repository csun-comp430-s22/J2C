package parser;

public class StringType implements Type {
   public boolean equals(final Object other) {
      return (other instanceof StringType);
   }
    public int hashCode() {
        return 241;
    }
    public String toString() {
        return "StringType()";
    }

}

package lexer;

public class AssignmentToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof AssignmentToken;
        }
        public int hashCode() {
            return 38;
        }
        
        public String toString() {
            return "=";
        }
        
    
}

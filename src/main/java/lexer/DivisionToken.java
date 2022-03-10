package lexer;

public class DivisionToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof DivisionToken;
        }
        public int hashCode() {
            return 31;
        }
        
        public String toString() {
            return "/";
        }
        
    
}

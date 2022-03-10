package lexer;

public class ReturnToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof ReturnToken;
        }
        public int hashCode() {
            return 35;
        }
        
        public String toString() {
            return "return";
        }
        
    
}

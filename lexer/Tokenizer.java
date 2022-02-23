package lexer;
import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    public static List<Token> tokenize(final String input) throws TokenizerException {
        List<Token> tokens = new ArrayList<Token>();
        int offset = 0;
        while(offset < input.length()) {
            if(input.startsWith("true")) {
                tokens.add(new TrueToken());
            } else {
                throw new TokenizerException();
            }
        }
        
        return tokens;

    }
    
}

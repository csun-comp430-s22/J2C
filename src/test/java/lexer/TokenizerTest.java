package lexer;
import java.util.List;

public class TokenizerTest {
    public static void testEmptyString() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assert(tokens.size() == 0);
    }
    public static void testOnlyWhiteSpace() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("   ");
        List<Token> tokens = tokenizer.tokenize();
        assert(tokens.size() == 0);
    }
    public static void main(String[] args) throws TokenizerException {
        testOnlyWhiteSpace();
        testEmptyString();

    }
}

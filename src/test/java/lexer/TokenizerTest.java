package lexer;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenizerTest {
    @Test
    public void testEmptyString() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
        //assertTrue(tokens.size() == 0);
    }
    @Test
    public void testOnlyWhiteSpace() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("   ");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
        //assertTrue(tokens.size() == 0);
    }
    @Test
    public void testTrueByItself() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("true");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1, tokens.size());
        Token trueToken = tokens.get(0);
        assertTrue(trueToken instanceof TrueToken);
    }
    // public static void main(String[] args) throws TokenizerException {
    //     testOnlyWhiteSpace();
    //     testEmptyString();

    // }
}

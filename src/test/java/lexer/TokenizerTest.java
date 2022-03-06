package lexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenizerTest {
    public void assertTokenizes(final String input,
                                final Token[] expected) {
        try {
            final Tokenizer tokenizer = new Tokenizer(input);
            final List<Token> received = tokenizer.tokenize();
            assertArrayEquals(expected,
                              received.toArray(new Token[received.size()]));
        } catch (final TokenizerException e) {
            fail("Tokenizer threw exception");
        }
    }
    
    @Test
    public void testEmptyString() {
        assertTokenizes("", new Token[0]);
    }

    @Test
    public void testOnlyWhitespace() throws TokenizerException {
        assertTokenizes("    ", new Token[0]);
    }

    @Test
    public void testTrueByItself() throws TokenizerException {
        assertTokenizes("true",
                        new Token[] { new TrueToken() });
    }
}
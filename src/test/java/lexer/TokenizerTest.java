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
    public void testOnlyWhitespace() {
        assertTokenizes("    ", new Token[0]);
    }

    @Test
    public void testTrueByItself() {
        assertTokenizes("true",
                        new Token[] { new TrueToken() });
    }

    @Test
    public void testVariable() {
        assertTokenizes("foo",
                        new Token[] { new VariableToken("foo") });
    }


    @Test
    public void testTrueTrueIsVariable() {
        assertTokenizes("truetrue",
                        new Token[] { new VariableToken("truetrue")});
    }

    @Test
    public void testTrueSpaceTrueAreTokens() {
        assertTokenizes("true true",
                        new Token[] { new TrueToken(),
                                      new TrueToken() });
    }

    @Test
    public void testLeftParen() {
        assertTokenizes("(",
                        new Token[] { new LeftParenToken() });
    }

    @Test
    public void IfToken() {
        assertTokenizes("if",
                        new Token[] { new IfToken() });
    }
    @Test
    public void ElseToken() {
        assertTokenizes("else",
                        new Token[] { new ElseToken() });
    }

    @Test
    public void testLeftCurly() {
        assertTokenizes("{",
                        new Token[] { new LeftCurlyToken() });
    }

    @Test
    public void testRightCurly() {
        assertTokenizes("}",
                        new Token[] { new RightCurlyToken() });
    }

    @Test
    public void testRightParen() {
        assertTokenizes(")",
                        new Token[] { new RightParenToken() });
    }
    // @Test
    // public void testModulo() {
    //     assertTokenizes("%",
    //                     new Token[] { new ModuloToken() });
    // }
    // @Test
    // public void testGreaterThan() {
    //     assertTokenizes(">",
    //                     new Token[] { new GreaterThanToken() });
    // }
    @Test
    public void testFalseByItself() {
        assertTokenizes("false",
                        new Token[] { new FalseToken() });
    }

    @Test
    public void testAddition() {
        assertTokenizes("+",
                        new Token[] { new AdditionToken() });
    }
    @Test
    public void testModulo() {
        assertTokenizes("%",
                        new Token[] { new ModuloToken() });
    }

    @Test
    public void testNot() {
        assertTokenizes("!",
                        new Token[] { new NotToken() });
    }

    @Test
    public void testGreaterThan() {
        assertTokenizes(">",
                        new Token[] { new GreaterThanToken() });
    }
    @Test
    public void testLessThan() {
        assertTokenizes("<",
                        new Token[] { new LessThanToken() });
    }

    @Test
    public void testClassToken() {
        assertTokenizes("class",
                        new Token[] { new ClassToken() });
    }
    @Test
    public void testWhileToken() {
        assertTokenizes("while",
                        new Token[] { new WhileToken() });
    }

    @Test
    public void testMultiplication() {
        assertTokenizes("*",
                        new Token[] { new MultiplicationToken() });
    }
    @Test
    public void testSubtraction() {
        assertTokenizes("-",
                        new Token[] { new SubtractionToken() });
    }
    @Test
    public void testSemiColon() {
        assertTokenizes(";",
                        new Token[] { new SemiColonToken() });
    }

    @Test
    public void testSingleDigitInteger() {
        assertTokenizes("1",
                        new Token[] { new IntegerToken(1) });
    }

    @Test
    public void testMultiDigitInteger() {
        assertTokenizes("123",
                        new Token[] { new IntegerToken(123) });
    }
    







    
}
package lexer;

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenizerTest {
    public void assertTokenizes(final String input,
            final Token[] expected) throws TokenizerException {

        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected,
                received.toArray(new Token[received.size()]));

    }

    @Test
    public void testEmptyString() throws TokenizerException {
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

    @Test
    public void testVariable() throws TokenizerException {
        assertTokenizes("foo",
                new Token[] { new VariableToken("foo") });
    }

    @Test
    public void testTrueTrueIsVariable() throws TokenizerException {
        assertTokenizes("truetrue",
                new Token[] { new VariableToken("truetrue") });
    }

    @Test
    public void testTrueSpaceTrueAreTokens() throws TokenizerException {
        assertTokenizes("true true",
                new Token[] { new TrueToken(),
                        new TrueToken() });
    }

    @Test
    public void testLeftParen() throws TokenizerException {
        assertTokenizes("(",
                new Token[] { new LeftParenToken() });
    }

    @Test
    public void IfToken() throws TokenizerException {
        assertTokenizes("if",
                new Token[] { new IfToken() });
    }

    @Test
    public void ElseToken() throws TokenizerException {
        assertTokenizes("else",
                new Token[] { new ElseToken() });
    }

    @Test
    public void testLeftCurly() throws TokenizerException {
        assertTokenizes("{",
                new Token[] { new LeftCurlyToken() });
    }

    @Test
    public void testRightCurly() throws TokenizerException {
        assertTokenizes("}",
                new Token[] { new RightCurlyToken() });
    }

    @Test
    public void testRightParen() throws TokenizerException {
        assertTokenizes(")",
                new Token[] { new RightParenToken() });
    }

    // @Test
    // public void testModulo() {
    // assertTokenizes("%",
    // new Token[] { new ModuloToken() });
    // }
    // @Test
    // public void testGreaterThan() {
    // assertTokenizes(">",
    // new Token[] { new GreaterThanToken() });
    // }
    @Test
    public void testFalseByItself() throws TokenizerException {
        assertTokenizes("false",
                new Token[] { new FalseToken() });
    }

    @Test
    public void testAddition() throws TokenizerException {
        assertTokenizes("+",
                new Token[] { new AdditionToken() });
    }

    @Test
    public void testModulo() throws TokenizerException {
        assertTokenizes("%",
                new Token[] { new ModuloToken() });
    }

    @Test
    public void testNot() throws TokenizerException {
        assertTokenizes("!",
                new Token[] { new NotToken() });
    }

    @Test
    public void testGreaterThan() throws TokenizerException {
        assertTokenizes(">",
                new Token[] { new GreaterThanToken() });
    }

    @Test
    public void testLessThan() throws TokenizerException {
        assertTokenizes("<",
                new Token[] { new LessThanToken() });
    }

    @Test
    public void testClassToken() throws TokenizerException {
        assertTokenizes("class",
                new Token[] { new ClassToken() });
    }

    @Test
    public void testWhileToken() throws TokenizerException {
        assertTokenizes("while",
                new Token[] { new WhileToken() });
    }

    @Test
    public void testMultiplication() throws TokenizerException {
        assertTokenizes("*",
                new Token[] { new MultiplicationToken() });
    }

    @Test
    public void testSubtraction() throws TokenizerException {
        assertTokenizes("-",
                new Token[] { new SubtractionToken() });
    }

    @Test
    public void testSemiColon() throws TokenizerException {
        assertTokenizes(";",
                new Token[] { new SemiColonToken() });
    }

    @Test
    public void testSingleDigitInteger() throws TokenizerException {
        assertTokenizes("1",
                new Token[] { new IntegerToken(1) });
    }

    @Test
    public void testMultiDigitInteger() throws TokenizerException {
        assertTokenizes("123",
                new Token[] { new IntegerToken(123) });
    }

    @Test(expected = TokenizerException.class)
    public void testInvalidInput() throws TokenizerException {
        assertTokenizes("$",
                null);
    }

   

    @Test
    public void testThisToken() throws TokenizerException {
        assertTokenizes("this",
                new Token[] { new ThisToken() });
    }

    @Test
    public void testExtendsToken() throws TokenizerException {
        assertTokenizes("extends",
                new Token[] { new ExtendsToken() });
    }

    @Test
    public void testNewToken() throws TokenizerException {
        assertTokenizes("new",
                new Token[] { new NewToken() });
    }

    @Test
    public void testReturnToken() throws TokenizerException {
        assertTokenizes("return",
                new Token[] { new ReturnToken() });
    }

    @Test
    public void testBreakToken() throws TokenizerException {
        assertTokenizes("break",
                new Token[] { new BreakToken() });
    }

    @Test
    public void testDivision() throws TokenizerException {
        assertTokenizes("/",
                new Token[] { new DivisionToken() });
    }

    @Test
    public void testVoidTypeToken() throws TokenizerException {
        assertTokenizes("void",
                new Token[] { new VoidToken() });
    }

    @Test
    public void testBooleanTypeToken() throws TokenizerException {
        assertTokenizes("bool",
                new Token[] { new BooleanToken() });
    }

    @Test
    public void testPrintlnToken() throws TokenizerException {
        assertTokenizes("println",
                new Token[] { new PrintlnToken() });
    }

    @Test
    public void StringTypeToken() throws TokenizerException {
        assertTokenizes("string",
                new Token[] { new StringToken() });
    }

    @Test
    public void IntTypeToken() throws TokenizerException {
        assertTokenizes("int",
                new Token[] { new IntToken() });
    }

    @Test
    public void testIntegerFalse() throws TokenizerException {
        assertFalse(new IntegerToken(0).equals(Boolean.TYPE));
    }

    @Test
    public void testIntegerHash() throws TokenizerException {
        assertEquals(new IntegerToken(1).value, new IntegerToken(1).hashCode());
    }

    @Test
    public void testAssignmentToken() throws TokenizerException {
        assertTokenizes("=",
                new Token[] { new AssignmentToken() });
    }

    @Test
    public void testEqualsToken() throws TokenizerException {
        assertTokenizes("==",
                new Token[] { new EqualsToken() });
    }
    @Test
    public void testNotEqualsToken() throws TokenizerException {
        assertTokenizes("!=",
                new Token[] { new NotEqualsToken() });
    }
    @Test
    public void testGreaterThanEqualsToken() throws TokenizerException {
        assertTokenizes(">=",
                new Token[] { new GreaterThanOrEqualToken() });
    }

    @Test
    public void testLessThanEqualsToken() throws TokenizerException {
        assertTokenizes("<=",
                new Token[] { new LessThanOrEqualToken() });
    }

    


    

}


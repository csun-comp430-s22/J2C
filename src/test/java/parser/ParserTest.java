package parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import lexer.*;

import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testEqualsOpExp() {
        final OpExp first = new OpExp(new IntegerLiteralExp(1),
                                      new PlusOp(),
                                      new IntegerLiteralExp(1));
        final OpExp second = new OpExp(new IntegerLiteralExp(1),
                                      new PlusOp(),
                                      new IntegerLiteralExp(1));
        assertEquals(first, second);
    }
    @Test
    public void testDoubleEqualsOp() throws ParserException {
        final OpExp first = new OpExp(new IntegerLiteralExp(1),
                                      new DoubleEqualsOp(),
                                      new IntegerLiteralExp(1));
        final OpExp second = new OpExp(new IntegerLiteralExp(1),
                                      new DoubleEqualsOp(),
                                      new IntegerLiteralExp(1));
        assertEquals(first, second);
    }
    @Test
    public void testNotEqualsOp() throws ParserException {
        final OpExp first = new OpExp(new IntegerLiteralExp(1),
                                      new NotEqualsOp(),
                                      new IntegerLiteralExp(1));
        final OpExp second = new OpExp(new IntegerLiteralExp(1),
                                      new NotEqualsOp(),
                                      new IntegerLiteralExp(1));
        assertEquals(first, second);
    }

    @Test
    public void testPrimaryVariable() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
        assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")),
                                          1),
                     parser.parsePrimaryExp(0));
    }

    @Test
    public void testPrimaryInteger() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                     parser.parsePrimaryExp(0));
    }


    @Test
    public void testPrimaryParens() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftParenToken(),
                                                       new IntegerToken(123),
                                                       new RightParenToken()));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 3),
                     parser.parsePrimaryExp(0));
    }

    @Test
    public void testAdditiveOpPlus() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new AdditionToken()));
        assertEquals(new ParseResult<Op>(new PlusOp(), 1),
                     parser.parseAdditiveOp(0));
    }
                                                       
    @Test  //test failing
    public void testAdditiveOpMinus() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        assertEquals(new ParseResult<Op>(new SubtractionOp(), 1),
                     parser.parseAdditiveOp(0));
    }

    @Test
    public void testAdditiveOpDivide() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new DivisionToken()));
        assertEquals(new ParseResult<Op>(new DivisionOp(), 1),
                     parser.parseAdditiveOp(0));
    }

    @Test
    public void testAdditiveOpMultiply() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
        assertEquals(new ParseResult<Op>(new MultiplicationOp(), 1),
                     parser.parseAdditiveOp(0));
    }

    @Test
    public void testAdditiveExpOnlyPrimary() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                     parser.parseAdditiveExp(0));
    }

    @Test
    public void testAdditiveExpSingleOperator() throws ParserException {
        // 1 + 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new AdditionToken(),
                                                       new IntegerToken(2)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
                                                    new PlusOp(),
                                                    new IntegerLiteralExp(2)),
                                          3),
                     parser.parseAdditiveExp(0));
    }

    @Test
    public void testAdditiveExpMultiOperator() throws ParserException {
        // 1 + 2 - 3 ==> (1 + 2) - 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new AdditionToken(),
                                                       new IntegerToken(2),
                                                       new SubtractionToken(),
                                                       new IntegerToken(3)));
        final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                                                 new PlusOp(),
                                                 new IntegerLiteralExp(2)),
                                       new SubtractionOp(),
                                       new IntegerLiteralExp(3));
        assertEquals(new ParseResult<Exp>(expected, 5),
                     parser.parseAdditiveExp(0));
    }

    @Test
    public void testLessThanExpOnlyAdditive() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testLessThanSingleOperator() throws ParserException {
        // 1 < 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new LessThanOp(),
                                       new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testLessThanMultiOperator() throws ParserException {
        // 1 < 2 < 3 ==> (1 < 2) < 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2),
                                                       new LessThanToken(),
                                                       new IntegerToken(3)));
        final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                                                 new LessThanOp(),
                                                 new IntegerLiteralExp(2)),
                                       new LessThanOp(),
                                       new IntegerLiteralExp(3));
        assertEquals(new ParseResult<Exp>(expected, 5),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testLessThanMixedOperator() throws ParserException {
        // 1 < 2 + 3 ==> 1 < (2 + 3)
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2),
                                                       new AdditionToken(),
                                                       new IntegerToken(3)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new LessThanOp(),
                                       new OpExp(new IntegerLiteralExp(2),
                                                 new PlusOp(),
                                                 new IntegerLiteralExp(3)));
        assertEquals(new ParseResult<Exp>(expected, 5),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testGreaterThanSingleOperator() throws ParserException {
        // 1 > 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new GreaterThanToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new GreaterThanOp(),
                                       new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseGreaterThanExp(0));
    }

    @Test
    public void testGreaterThanMultiOperator() throws ParserException {
        // 1 > 2 > 3 ==> (1 > 2) > 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new GreaterThanToken(),
                                                       new IntegerToken(2),
                                                       new GreaterThanToken(),
                                                       new IntegerToken(3)));
        final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                                                 new GreaterThanOp(),
                                                 new IntegerLiteralExp(2)),
                                       new GreaterThanOp(),
                                       new IntegerLiteralExp(3));
        assertEquals(new ParseResult<Exp>(expected, 5),
                     parser.parseGreaterThanExp(0));
    }
    

}
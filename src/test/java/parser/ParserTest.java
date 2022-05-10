package parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import lexer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testTypeString() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StrToken()));
        assertEquals(new ParseResult<Type>(new StringType(), 1), parser.parseType(0));
    }

    @Test
    public void testTypeInt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken()));
        assertEquals(new ParseResult<Type>(new IntType(), 1), parser.parseType(0));

    }

    @Test
    public void testTypeVariableClass() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo")));
        assertEquals(new ParseResult<Type>(new ClassNameType(new ClassName("foo")), 1), parser.parseType(0));

    }

    @Test
    public void testTypeBool() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new BooleanToken()));
        assertEquals(new ParseResult<Type>(new BoolType(), 1), parser.parseType(0));

    }

    @Test
    public void testDoubleEqualsOp() throws ParserException {
        final OpExp first = new OpExp(new IntegerLiteralExp(1),
                new EqualsOp(),
                new IntegerLiteralExp(1));
        final OpExp second = new OpExp(new IntegerLiteralExp(1),
                new EqualsOp(),
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
    public void testPrimaryString() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StringToken("hello")));
        assertEquals(new ParseResult<Exp>(new StringLiteralExp("hello"), 1),
                parser.parsePrimaryExp(0));
    }

    @Test
    public void testPrimaryTrue() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new TrueToken()));
        assertEquals(new ParseResult<Exp>(new TrueExp(), 1),
                parser.parsePrimaryExp(0));
    }

    @Test
    public void testPrimaryFalse() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new FalseToken()));
        assertEquals(new ParseResult<Exp>(new FalseExp(), 1),
                parser.parsePrimaryExp(0));
    }

    @Test
    public void testMultiplicativeOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
        assertEquals(new ParseResult<Op>(new MultiplicationOp(), 1), parser.parseMultiplicativeOp(0));
    }

    @Test
    public void testPlusOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new AdditionToken()));
        assertEquals(new ParseResult<Op>(new PlusOp(), 1), parser.parseAdditiveOp(0));
    }

    @Test
    public void testMinusOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        assertEquals(new ParseResult<Op>(new SubtractionOp(), 1), parser.parseAdditiveOp(0));
    }

    @Test
    public void testDivisionOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new DivisionToken()));
        assertEquals(new ParseResult<Op>(new DivisionOp(), 1), parser.parseMultiplicativeOp(0));
    }

    @Test
    public void testMultiplicativeExpOnlyPrimary() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1), parser.parseMultiplicativeExp(0));
    }

    @Test
    public void testMultiplicativeExpSingleOperator() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new MultiplicationToken(),
                new IntegerToken(2)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
                new MultiplicationOp(),
                new IntegerLiteralExp(2)),
                3),
                parser.parseMultiplicativeExp(0));
    }

    @Test
    public void testMultiplicativeExpMultiOperator() throws ParserException {
        // 1 + 2 - 3 ==> (1 + 2) - 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new MultiplicationToken(),
                new IntegerToken(2),
                new DivisionToken(),
                new IntegerToken(3)));
        final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                new MultiplicationOp(),
                new IntegerLiteralExp(2)),
                new DivisionOp(),
                new IntegerLiteralExp(3));
        assertEquals(new ParseResult<Exp>(expected, 5),
                parser.parseMultiplicativeExp(0));
    }

    @Test
    public void testAdditiveExpOnlyPrimary() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1), parser.parseAdditiveExp(0));
    }

    @Test
    public void testAdditiveExpSingleOperator() throws ParserException {
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
    public void testLessThanOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LessThanToken()));
        assertEquals(new ParseResult<Op>(new LessThanOp(), 1), parser.parseComparisonOp(0));
    }

    @Test
    public void testGreaterThanOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new GreaterThanToken()));
        assertEquals(new ParseResult<Op>(new GreaterThanOp(), 1), parser.parseComparisonOp(0));
    }

    @Test
    public void testComparisonNotEqualsOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new NotEqualsToken()));
        assertEquals(new ParseResult<Op>(new NotEqualsOp(), 1), parser.parseComparisonOp(0));
    }

    @Test
    public void testComparisonEqualsOp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new EqualsToken()));
        assertEquals(new ParseResult<Op>(new EqualsOp(), 1), parser.parseComparisonOp(0));
    }

    @Test
    public void testAssertToken() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.assertTokenHereIs(0, new SubtractionToken());
    }

    @Test(expected = ParserException.class)
    public void testAssertTokenException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.assertTokenHereIs(0, new AdditionToken());
    }

    @Test(expected = ParserException.class)
    public void testParseTypeException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.parseType(0);
    }

    @Test(expected = ParserException.class)
    public void testParsePrimaryExpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.parsePrimaryExp(0);
    }

    @Test(expected = ParserException.class)
    public void testAdditiveOpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.parseAdditiveOp(3);
    }

    @Test(expected = ParserException.class)
    public void testComparisonOpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.parseComparisonOp(0);
    }

    @Test(expected = ParserException.class)
    public void testParseExpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
        parser.parseExp(0);
    }

    // @Test(expected = ParserException.class)
    // public void testVarDecException() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
    // parser.parseVarDec(0);
    // }

    // @Test(expected = ParserException.class)
    // public void testParseThisStmtException() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
    // parser.parseThisStmt(0);
    // }
    // @Test(expected = ParserException.class)
    // public void testBreakStmtException() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
    // parser.parseBreakStmt(0);
    // }

    // @Test(expected = ParserException.class)
    // public void testParseStmtException() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
    // parser.parseStmt(0);
    // }

    @Test
    public void testComparisonExpOnlyPrimary() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1), parser.parseComparisonExp(0));
    }

    @Test
    public void testComparisonExpSingleOperator() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new LessThanToken(),
                new IntegerToken(3)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),

                new LessThanOp(),
                new IntegerLiteralExp(3)),
                3),
                parser.parseComparisonExp(0));
    }

    // 1 != 2
    @Test
    public void testParseExpNotEquals() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new NotEqualsToken(),
                new IntegerToken(2)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
                new NotEqualsOp(),
                new IntegerLiteralExp(2)),
                3),
                parser.parseExp(0));
    }

    // 2 == 3
    public void testParseExpEquals() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(2),
                new EqualsToken(),
                new IntegerToken(3)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(2),
                new EqualsOp(),
                new IntegerLiteralExp(3)),
                3),
                parser.parseExp(0));
    }

    @Test
    public void testParseExpForComparisonExp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new LessThanToken(),
                new IntegerToken(3)));
        final ParseResult<Exp> expected = new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
                new LessThanOp(),
                new IntegerLiteralExp(3)), 3);
        assertEquals(expected, parser.parseComparisonExp(0));
    }

    @Test(expected = ParserException.class)
    public void testParseClassNameExpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                new LessThanToken(),
                new IntegerToken(3)));
        parser.parseClassNameExp(0);
    }

    @Test
    public void testParseClassName() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("Foo")));
        final ParseResult<Exp> expected = new ParseResult<Exp>(new ClassNameExp(new ClassName("Foo")), 1);
        assertEquals(expected, parser.parseClassNameExp(0));
    }

    // new Baz()
    @Test
    public void testParseNewClassExpWithoutParams() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new NewToken(), new VariableToken("Baz"), new LeftParenToken(), new RightParenToken()));
        List<Exp> inside = new ArrayList<Exp>();
        final ParseResult<Exp> expected = new ParseResult<Exp>(
                new NewClassNameExp(new ClassNameExp(new ClassName("Baz")), inside), 4);
        assertEquals(expected, parser.parseNewClassNameExp(0));
    }

    // new Baz(2)
    @Test
    public void testParseNewClassExpWithParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Baz"), new LeftParenToken(),
                new IntegerToken(2), new RightParenToken()));
        List<Exp> inside = new ArrayList<Exp>();
        inside.add(new IntegerLiteralExp(2));
        final ParseResult<Exp> expected = new ParseResult<Exp>(
                new NewClassNameExp(new ClassNameExp(new ClassName("Baz")), inside), 5);
        assertEquals(expected, parser.parseNewClassNameExp(0));
    }

    // new Baz(2, 3)
    @Test
    public void testParseNewClassExpWithMultipleParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Baz"), new LeftParenToken(),
                new IntegerToken(2), new CommaToken(), new IntegerToken(3), new RightParenToken()));
        List<Exp> inside = new ArrayList<Exp>();
        inside.add(new IntegerLiteralExp(2));
        inside.add(new IntegerLiteralExp(3));
        final ParseResult<Exp> expected = new ParseResult<Exp>(
                new NewClassNameExp(new ClassNameExp(new ClassName("Baz")), inside), 7);
        assertEquals(expected, parser.parseNewClassNameExp(0));
    }

    // foo.bar()
    @Test
    public void testVarMethodCallExp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inside = new ArrayList<Exp>();
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inside), 5), parser.parseVarMethodNameCall(0));
    }

    @Test(expected = ParserException.class)
    public void testParseMethodNameException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new RightParenToken()));
        parser.parseMethodName(4);
    }

    // foo.bar(baz)
    @Test
    public void testVarMethodCallWithVariable() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> param = new ArrayList<Exp>();
        param.add(new VariableExp(new Variable("baz")));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, param), 6), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(1)
    @Test
    public void testVarMethodCallWithInteger() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new IntegerToken(1), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> param = new ArrayList<Exp>();
        param.add(new IntegerLiteralExp(1));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, param), 6), parser.parseVarMethodNameCall(0));
    }

    // foo.bar("hello")
    @Test
    public void testVarMethodCallWithString() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new StringToken("hello"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        final ParseResult<Exp> param = new ParseResult<Exp>(new StringLiteralExp("hello"), 1);
        inner.add(param.result);
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 6), parser.parseVarMethodNameCall(0));

    }

    // foo.bar(true)
    @Test
    public void testVarMethodCallWithTrueParam() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new TrueToken(), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        final ParseResult<Exp> param = new ParseResult<Exp>(new TrueExp(), 1);
        inner.add(param.result);
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 6), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(false)
    @Test
    public void testVarMethodCallWithFalseParam() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new FalseToken(), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        final ParseResult<Exp> param = new ParseResult<Exp>(new FalseExp(), 1);
        inner.add(param.result);
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 6), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(baz, 25)
    @Test
    public void testVarMethodCallWithVarAndIntParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new CommaToken(),
                new IntegerToken(25), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new IntegerLiteralExp(25));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 8), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(baz, 25, "hello")
    @Test
    public void testVarMethodCallWithVarAndIntAndStringParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new CommaToken(),
                new IntegerToken(25), new CommaToken(), new StringToken("hello"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new IntegerLiteralExp(25));
        inner.add(new StringLiteralExp("hello"));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 10), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(true, "hello", baz)
    @Test
    public void testVarMethodCallWithTrueAndStringAndVarParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new TrueToken(), new CommaToken(),
                new StringToken("hello"), new CommaToken(), new VariableToken("baz"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new TrueExp());
        inner.add(new StringLiteralExp("hello"));
        inner.add(new VariableExp(new Variable("baz")));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 10), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(true, "hello", baz, 25)
    @Test
    public void testVarMethodCallWithTrueAndStringAndVarAndIntParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new TrueToken(), new CommaToken(),
                new StringToken("hello"), new CommaToken(), new VariableToken("baz"), new CommaToken(),
                new IntegerToken(25), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new TrueExp());
        inner.add(new StringLiteralExp("hello"));
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new IntegerLiteralExp(25));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 12), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(baz, woo)
    @Test
    public void testVarMethodCallWithVarAndVarParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new CommaToken(),
                new VariableToken("woo"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new VariableExp(new Variable("woo")));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 8), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(baz, false)
    @Test
    public void testVarMethodCallWithVarAndFalseParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new CommaToken(),
                new FalseToken(), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new FalseExp());
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 8), parser.parseVarMethodNameCall(0));
    }

    // foo.bar(baz, true, "hello")
    @Test
    public void testVarMethodCallWithVarAndTrueAndStringParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new VariableToken("baz"), new CommaToken(),
                new TrueToken(), new CommaToken(), new StringToken("hello"), new RightParenToken()));
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        List<Exp> inner = new ArrayList<Exp>();
        inner.add(new VariableExp(new Variable("baz")));
        inner.add(new TrueExp());
        inner.add(new StringLiteralExp("hello"));
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 10), parser.parseVarMethodNameCall(0));
    }

    @Test
    public void testVarMethodCallThruParseExp() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new RightParenToken()));
        final Exp variable = new VariableExp(new Variable("foo"));
        final MethodNameExp name = new MethodNameExp(new MethodName("bar"));
        final List<Exp> inside = new ArrayList<Exp>();
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(variable, name, inside), 5), parser.parseExp(0));
    }

    @Test(expected = ParserException.class)
    public void testVarMethodCallException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
                new VariableToken("bar"), new LeftParenToken(), new LessThanToken(), new RightParenToken()));
        parser.parseVarMethodNameCall(0);
    }

    @Test(expected = ParserException.class)
    public void testVarMethodCallExpWithError() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new VariableToken("foo"), new PeriodToken(), new VariableToken("bar"),
                        new LeftParenToken(), new VariableToken("baz"), new LessThanToken(), new VariableToken("foo"),
                        new RightParenToken())); // missing comma token
        final Exp foo = new VariableExp(new Variable("foo"));
        final MethodNameExp bar = new MethodNameExp(new MethodName("bar"));
        final List<Exp> inner = new ArrayList<Exp>();
        final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("baz")), 1);
        final ParseResult<Exp> param2 = new ParseResult<Exp>(new VariableExp(new Variable("foo")), 1);
        inner.add(param.result);
        inner.add(param2.result);
        assertEquals(new ParseResult<Exp>(new VarMethodCallExp(foo, bar, inner), 8), parser.parseVarMethodNameCall(0));

    }

    @Test(expected = ParserException.class)
    public void testParseComparisonOpException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new AdditionToken()));
        parser.parseComparisonOp(0);
    }

    @Test(expected = ParserException.class)
    public void testParseComparisonExpThruParseExpException() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new VariableToken("foo"), new NotEqualsToken(), new VariableToken("bar")));
        parser.parseExp(4);
    }

    // int foo = 24;
    @Test
    public void testParseVardecIntType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new AssignmentToken(),
                new IntegerToken(3), new SemiColonToken()));
        final Type type = new IntType();
        final Exp variable = new VariableExp(new Variable("x"));
        final Exp exp = new IntegerLiteralExp(3);
        assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5),
                parser.parseVarDec(0));
    }

    // boolean foo = true;
    @Test
    public void testParseVardecBooleanType() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new BooleanToken(), new VariableToken("x"), new AssignmentToken(),
                        new TrueToken(), new SemiColonToken()));
        final Type type = new BoolType();
        final Exp variable = new VariableExp(new Variable("x"));
        final Exp exp = new TrueExp();
        assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5),
                parser.parseVarDec(0));
    }

    // String foo = "hello";
    @Test
    public void testParseVardecStringType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StrToken(), new VariableToken("x"), new AssignmentToken(),
                new StringToken("hello"), new SemiColonToken()));
        final Type type = new StringType();
        final Exp variable = new VariableExp(new Variable("x"));
        final Exp exp = new StringLiteralExp("hello");
        assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5),
                parser.parseVarDec(0));
    }

    // Baz foo = new Baz();
    @Test
    public void testParseVardecClassType() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new VariableToken("Baz"), new VariableToken("foo"), new AssignmentToken(),
                        new NewToken(), new VariableToken("Baz"), new LeftParenToken(), new RightParenToken(),
                        new SemiColonToken()));
        final Type type = new ClassNameType(new ClassName("Baz"));
        final Exp variable = new VariableExp(new Variable("foo"));
        final List<Exp> emptyParams = new ArrayList<Exp>();
        final Exp exp = new NewClassNameExp(new ClassNameExp(new ClassName("Baz")), emptyParams);
        assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 8),
                parser.parseVarDec(0));
    }

    // > foo = 4;
    @Test(expected = ParserException.class)
    public void testParseVardecExpressionType() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new GreaterThanToken(), new VariableToken("x"), new AssignmentToken(),
                        new IntegerToken(3), new SemiColonToken()));
        parser.parseVarDec(0);
    }

    // int foo < 33;
    @Test(expected = ParserException.class)
    public void testParseVardecExpressionType2() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new LessThanToken(),
                new IntegerToken(3), new SemiColonToken()));
        parser.parseVarDec(0);
    }

    // int foo
    @Test
    public void testIntTypeParam() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("foo")));
        final ParseResult<Param> expected = new ParseResult<Param>(
                new Param(new IntType(), new VariableExp(new Variable("foo"))), 2);

        assertEquals(expected, parser.parseParam(0));
    }

    // Foo bar
    @Test
    public void testClassTypeParam() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("Foo"), new VariableToken("bar")));
        final ParseResult<Param> expected = new ParseResult<Param>(
                new Param(new ClassNameType(new ClassName("Foo")), new VariableExp(new Variable("bar"))), 2);

        assertEquals(expected, parser.parseParam(0));
    }

    // string baz
    @Test
    public void testStringTypeParam() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StrToken(), new VariableToken("baz")));
        final ParseResult<Param> expected = new ParseResult<Param>(
                new Param(new StringType(), new VariableExp(new Variable("baz"))), 2);

        assertEquals(expected, parser.parseParam(0));
    }

    @Test(expected = ParserException.class)
    public void testParseParamException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new FalseToken()));
        parser.parseParam(0);
    }

    // int foo = 4;
    @Test
    public void testParseVardecThruStmtIntType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("foo"), new AssignmentToken(),
                new IntegerToken(4), new SemiColonToken()));
        final Type type = new IntType();
        final Exp variable = new VariableExp(new Variable("foo"));
        final Exp exp = new IntegerLiteralExp(4);
        final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(
                new VariableDeclaration(type, variable, exp), 5);
        assertEquals(new ParseResult<Stmt>(new VarDecStmt(variableDec), 5), parser.parseStmt(0));
    }

    // bool foo = true;
    @Test
    public void testParseVardecThruStmtBooleanType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new BooleanToken(), new VariableToken("foo"),
                new AssignmentToken(), new TrueToken(), new SemiColonToken()));
        final Type type = new BoolType();
        final Exp variable = new VariableExp(new Variable("foo"));
        final Exp exp = new TrueExp();
        final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(
                new VariableDeclaration(type, variable, exp), 5);
        assertEquals(new ParseResult<Stmt>(new VarDecStmt(variableDec), 5), parser.parseStmt(0));
    }

    // str foo = "hello";
    @Test
    public void testParseVardecThruStmtStringType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StrToken(), new VariableToken("foo"), new AssignmentToken(),
                new StringToken("hello"), new SemiColonToken()));
        final Type type = new StringType();
        final Exp variable = new VariableExp(new Variable("foo"));
        final Exp exp = new StringLiteralExp("hello");
        final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(
                new VariableDeclaration(type, variable, exp), 5);
        assertEquals(new ParseResult<Stmt>(new VarDecStmt(variableDec), 5), parser.parseStmt(0));
    }

    // break;
    @Test
    public void testParseBreak() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new BreakToken(), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BreakStmt("break", ";"), 2);
        assertEquals(expected, parser.parseBreakStmt(0));
    }

    @Test
    public void testBreakStatementThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new BreakToken(), new SemiColonToken()));

        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BreakStmt("break", ";"), 2);
        assertEquals(expected, parser.parseStmt(0));
    }

    // foo = 5;
    @Test
    public void testVarAssignThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new AssignmentToken(),
                new IntegerToken(5), new SemiColonToken()));
        final ParseResult<Exp> variable = new ParseResult<Exp>(new VariableExp(new Variable("foo")), 1);
        final ParseResult<Exp> exp = new ParseResult<Exp>(new IntegerLiteralExp(5), 1);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new VarAssignStmt(variable.result, exp.result), 4);
        assertEquals(expected, parser.parseStmt(0));
    }

    // while (foo < 10) x = 5;
    @Test
    public void testWhileStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new WhileToken(), new LeftParenToken(), new VariableToken("foo"),
                new LessThanToken(), new IntegerToken(10),
                new RightParenToken(), new VariableToken("foo"), new AssignmentToken(), new IntegerToken(5),
                new SemiColonToken()));
        final ParseResult<Exp> guard = new ParseResult<Exp>(
                new OpExp(new VariableExp(new Variable("foo")), new LessThanOp(), new IntegerLiteralExp(10)), 3);
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(
                new VarAssignStmt(new VariableExp(new Variable("foo")), new IntegerLiteralExp(5)), 4);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new WhileStmt(guard.result, stmt.result), 10);
        assertEquals(expected, parser.parseStmt(0));
    }

    // return foo;
    @Test
    public void testReturnStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ReturnToken(), new VariableToken("foo"), new SemiColonToken()));
        final ParseResult<Exp> exp = new ParseResult<Exp>(new VariableExp(new Variable("foo")), 1);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ReturnStmt(exp.result), 3);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test
    public void testBlockStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new VariableToken("x"),
                new AssignmentToken(), new VariableToken("y"), new SemiColonToken(), new RightCurlyToken()));
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(
                new VarAssignStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))), 3);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(stmt1.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 6);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test
    public void testIfStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IfToken(), new LeftParenToken(), new VariableToken("x"),
                new GreaterThanToken(), new IntegerToken(1), new RightParenToken(), new VariableToken("x"),
                new AssignmentToken(), new IntegerToken(0), new SemiColonToken(), new ElseToken(),
                new VariableToken("x"), new AssignmentToken(), new IntegerToken(1), new SemiColonToken()));
        final ParseResult<Exp> ifGuard = new ParseResult<Exp>(
                new OpExp(new VariableExp(new Variable("x")), new GreaterThanOp(), new IntegerLiteralExp(1)), 3);
        final ParseResult<Stmt> trueBranch = new ParseResult<Stmt>(
                new VarAssignStmt(new VariableExp(new Variable("x")), new IntegerLiteralExp(0)), 4);
        final ParseResult<Stmt> falseBranch = new ParseResult<Stmt>(
                new VarAssignStmt(new VariableExp(new Variable("x")), new IntegerLiteralExp(1)), 4);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), 15);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test
    public void testPrintlnInBlockThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new PrintlnToken(), new LeftParenToken(),
                new RightParenToken(), new SemiColonToken(), new RightCurlyToken()));
        final List<Exp> emptyPrint = new ArrayList<Exp>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(stmt1.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 6);
        assertEquals(expected, parser.parseStmt(0));
    }

    /*
     * { println();
     * println();
     * }
     */
    @Test
    public void testMultiplePrintlnInBlockThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new PrintlnToken(), new LeftParenToken(),
                new RightParenToken(), new SemiColonToken(),
                new PrintlnToken(), new LeftParenToken(), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken()));
        final List<Exp> emptyPrint = new ArrayList<Exp>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(stmt1.result);
        stmts.add(stmt1.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 10);
        assertEquals(expected, parser.parseStmt(0));
    }

    // { }
    @Test
    public void testEmptyBlockThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new RightCurlyToken()));
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        assertEquals(expected, parser.parseStmt(0));
    }

    /*
     * foo = bar;
     * println();
     */
    @Test
    public void testBlockStmtWithTwoStmts() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new VariableToken("x"),
                new AssignmentToken(), new VariableToken("y"), new SemiColonToken(),
                new PrintlnToken(), new LeftParenToken(), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken()));
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(
                new VarAssignStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))), 3);
        final List<Exp> emptyPrint = new ArrayList<Exp>();
        final ParseResult<Stmt> stmt2 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(stmt1.result);
        stmts.add(stmt2.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 10);
        assertEquals(expected, parser.parseStmt(0));
    }

    // super(foo);
    @Test
    public void testSuperStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SuperToken(), new LeftParenToken(), new VariableToken("foo"),
                new RightParenToken(), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                new SuperStmt("super", new VariableExp(new Variable("foo"))), 5);
        assertEquals(expected, parser.parseSuperStmt(0));

    }

    // super(foo);
    @Test
    public void testSuperStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new SuperToken(), new LeftParenToken(), new VariableToken("foo"),
                new RightParenToken(), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                new SuperStmt("super", new VariableExp(new Variable("foo"))), 5);
        assertEquals(expected, parser.parseStmt(0));
    }

    // this.foo = bar;
    @Test
    public void testThisStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ThisToken(), new PeriodToken(), new VariableToken("foo"),
                new AssignmentToken(), new VariableToken("bar"), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                new ThisStmt(new VariableExp(new Variable("foo")), new VariableExp(new Variable("bar"))), 6);
        assertEquals(expected, parser.parseThisStmt(0));
    }

    // foo;
    public void testExpStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("foo"))), 2);
        assertEquals(expected, parser.parseStmt(0));
    }

    // this.foo = bar;
    @Test
    public void testThisStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ThisToken(), new PeriodToken(), new VariableToken("foo"),
                new AssignmentToken(), new VariableToken("bar"), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                new ThisStmt(new VariableExp(new Variable("foo")), new VariableExp(new Variable("bar"))), 6);
        assertEquals(expected, parser.parseStmt(0));
    }

    // println("foo");
    @Test
    public void testPrintlnStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new LeftParenToken(), new StringToken("foo"),
                new RightParenToken(), new SemiColonToken()));
        final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StringLiteralExp("foo"), 1);
        final List<Exp> exps = new ArrayList<Exp>();
        exps.add(exp1.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 5);
        assertEquals(expected, parser.parseStmt(0));
    }

    // println("foo", "bar");
    @Test
    public void testPrintlnStmtWithTwoExps() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new LeftParenToken(), new StringToken("foo"),
                new CommaToken(), new StringToken("bar"), new RightParenToken(), new SemiColonToken()));
        final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StringLiteralExp("foo"), 1);
        final ParseResult<Exp> exp2 = new ParseResult<Exp>(new StringLiteralExp("bar"), 3);
        final List<Exp> exps = new ArrayList<Exp>();
        exps.add(exp1.result);
        exps.add(exp2.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 7);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test
    public void testPrintlnStmtThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new LeftParenToken(), new StringToken("foo"),
                new CommaToken(), new StringToken("bar"), new RightParenToken(), new SemiColonToken()));
        final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StringLiteralExp("foo"), 1);
        final ParseResult<Exp> exp2 = new ParseResult<Exp>(new StringLiteralExp("bar"), 3);
        final List<Exp> exps = new ArrayList<Exp>();
        exps.add(exp1.result);
        exps.add(exp2.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 7);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test
    public void testPrintlnStmtWithEmptyExps() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new PrintlnToken(), new LeftParenToken(), new RightParenToken(), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(new ArrayList<Exp>()), 4);
        assertEquals(expected, parser.parseStmt(0));
    }

    // println("foo", "bar", "baz");
    @Test
    public void testPrintlnStmtWithThreeExps() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new LeftParenToken(), new StringToken("foo"),
                new CommaToken(), new StringToken("bar"), new CommaToken(), new StringToken("baz"),
                new RightParenToken(), new SemiColonToken()));
        final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StringLiteralExp("foo"), 1);
        final ParseResult<Exp> exp2 = new ParseResult<Exp>(new StringLiteralExp("bar"), 3);
        final ParseResult<Exp> exp3 = new ParseResult<Exp>(new StringLiteralExp("baz"), 5);
        final List<Exp> exps = new ArrayList<Exp>();
        exps.add(exp1.result);
        exps.add(exp2.result);
        exps.add(exp3.result);
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 9);
        assertEquals(expected, parser.parseStmt(0));
    }

    @Test(expected = ParserException.class)
    public void testParseResultExceptionThruStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new LeftParenToken(), new StringToken("foo"),
                new CommaToken(), new StringToken("bar"), new CommaToken(), new StringToken("baz"),
                new RightParenToken(), new SemiColonToken()));
        final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(new ArrayList<Exp>()), 9);
        assertEquals(expected, parser.parseStmt(9));
    }

    /*
     * String Foo() {
     * 
     * }
     */
    @Test
    public void testParseMethodDefStringType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new StrToken(), new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new StringType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 6);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Int Foo() {
     * 
     * }
     */

    @Test
    public void testParseMethodDefIntType() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 6);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Foo Bar() {
     * 
     * }
     */
    @Test
    public void testParseMethodDefFooBar() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("Foo"), new VariableToken("Bar"),
                new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new ClassNameType(new ClassName("Foo")), 1);
        final MethodName methodname = new MethodName("Bar");
        final List<Param> params = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 6);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Int Foo() {
     * x;
     * }
     */
    @Test
    public void testParseMethodDefWithSingleStmt() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new IntToken(), new VariableToken("Foo"), new LeftParenToken(), new RightParenToken(),
                        new LeftCurlyToken(), new VariableToken("x"), new SemiColonToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("x"))), 1);
        stmts.add(stmt1.result);
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 8);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Int Foo(boolean x) {
     * y;
     * }
     */
    @Test
    public void testParseMethodDefParamStmt() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("Foo"), new LeftParenToken(),
                new BooleanToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
                new VariableToken("y"), new SemiColonToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final ParseResult<Param> param1 = new ParseResult<Param>(
                new Param(new BoolType(), new VariableExp(new Variable("x"))), 2);
        params.add(param1.result);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 1);
        stmts.add(stmt1.result);
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 10);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Int Foo(boolean x, int y) {
     * z;
     * }
     */
    @Test
    public void testParseMethodDefTwoParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("Foo"), new LeftParenToken(),
                new BooleanToken(), new VariableToken("x"), new CommaToken(), new IntToken(), new VariableToken("y"),
                new RightParenToken(), new LeftCurlyToken(), new VariableToken("z"), new SemiColonToken(),
                new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final ParseResult<Param> param1 = new ParseResult<Param>(
                new Param(new BoolType(), new VariableExp(new Variable("x"))), 2);
        final ParseResult<Param> param2 = new ParseResult<Param>(
                new Param(new IntType(), new VariableExp(new Variable("y"))), 3);
        params.add(param1.result);
        params.add(param2.result);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("z"))), 1);
        stmts.add(stmt1.result);
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 13);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    /*
     * Int Foo(boolean x, int y, boolean z) {
     * w;
     * }
     */
    @Test
    public void testParseMethodDefThreeParams() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("Foo"), new LeftParenToken(),
                new BooleanToken(), new VariableToken("x"), new CommaToken(), new IntToken(), new VariableToken("y"),
                new CommaToken(), new BooleanToken(), new VariableToken("z"), new RightParenToken(),
                new LeftCurlyToken(), new VariableToken("w"), new SemiColonToken(), new RightCurlyToken()));
        final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
        final MethodName methodname = new MethodName("Foo");
        final List<Param> params = new ArrayList<Param>();
        final ParseResult<Param> param1 = new ParseResult<Param>(
                new Param(new BoolType(), new VariableExp(new Variable("x"))), 2);
        final ParseResult<Param> param2 = new ParseResult<Param>(
                new Param(new IntType(), new VariableExp(new Variable("y"))), 3);
        final ParseResult<Param> param3 = new ParseResult<Param>(
                new Param(new BoolType(), new VariableExp(new Variable("z"))), 4);
        params.add(param1.result);
        params.add(param2.result);
        params.add(param3.result);
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("w"))), 1);
        stmts.add(stmt1.result);
        final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
        final ParseResult<MethodDef> expected = new ParseResult<MethodDef>(
                new MethodDef(type.result, methodname, params, stmt.result), 16);
        assertEquals(expected, parser.parseMethodDef(0));
    }

    @Test(expected = ParserException.class)
    public void testParseMethodException() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new IfToken()));
        parser.parseMethodDef(0);
    }

    /*
     * class Foo {
     * Foo()
     * 1 + 2;
     * }
     */
    public void testParseClassDefTest1() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(),
                new VariableToken("Foo"), new LeftParenToken(), new RightParenToken(),
                new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 11);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest2() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2),
                new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 13);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo {
     * String text = "Hello";
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest3() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new StrToken(),
                        new VariableToken("text"), new AssignmentToken(), new StringToken("Hello"),
                        new SemiColonToken(), new VariableToken("Foo"), new LeftParenToken(),
                        new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                        new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 16);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * String text = "Hello";
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest4() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new StrToken(), new VariableToken("text"),
                new AssignmentToken(), new StringToken("Hello"), new SemiColonToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 18);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo {
     * Int x = 5;
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest5() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(),
                new IntToken(), new VariableToken("x"), new AssignmentToken(), new IntegerToken(5),
                new SemiColonToken(),
                new VariableToken("Foo"), new LeftParenToken(), new RightParenToken(), new IntegerToken(1),
                new AdditionToken(), new IntegerToken(2), new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new IntType(), new VariableExp(new Variable("x")),
                new IntegerLiteralExp(5));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 16);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * Int x = 5;
     * Foo()
     * 1 + 2;
     * }
     */
    
    @Test
    public void testParseClassDefTest6() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new IntToken(), new VariableToken("x"),
                new AssignmentToken(), new IntegerToken(5), new SemiColonToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new IntType(), new VariableExp(new Variable("x")),
                new IntegerLiteralExp(5));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 18);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo() {
     * Boolean x = true;
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest7() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new BooleanToken(),
                        new VariableToken("x"), new AssignmentToken(), new FalseToken(), new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                        new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new BoolType(), new VariableExp(new Variable("x")),
                new FalseExp());
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 16);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar() {
     * Boolean x = false;
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseClassDefTest8() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                        new VariableToken("Bar"), new LeftCurlyToken(), new BooleanToken(),
                        new VariableToken("x"), new AssignmentToken(), new FalseToken(), new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                        new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new BoolType(), new VariableExp(new Variable("x")),
                new FalseExp());
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 18);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo {
     * String text "Hello World";
     * Foo(int x)
     * 1 + 2;
     * 
     * }
     */
    @Test
    public void testParseClassDefTest9() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new StrToken(),
                        new VariableToken("text"), new AssignmentToken(), new StringToken("Hello World"),
                        new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new IntToken(), new VariableToken("x"), new RightParenToken(), new IntegerToken(1),
                        new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello World"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Param param = new Param(new IntType(), new VariableExp(new Variable("x")));
        params.add(param);
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 18);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * String text "Hello World";
     * Foo(int x)
     * 1 + 2;
     * 
     * }
     */
    @Test
    public void testParseClassDefTest10() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                        new VariableToken("Bar"), new LeftCurlyToken(), new StrToken(),
                        new VariableToken("text"), new AssignmentToken(), new StringToken("Hello World"),
                        new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new IntToken(), new VariableToken("x"), new RightParenToken(), new IntegerToken(1),
                        new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello World"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Param param = new Param(new IntType(), new VariableExp(new Variable("x")));
        params.add(param);
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 20);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * 
     * Foo(int x, int y)
     * 1 + 2;
     * 
     * }
     * 
     */
    @Test
    public void testParseClassDefTest11() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"), new LeftParenToken(),
                new IntToken(), new VariableToken("x"), new CommaToken(), new IntToken(), new VariableToken("y"),
                new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(),
                new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Param param = new Param(new IntType(), new VariableExp(new Variable("x")));
        final Param param1 = new Param(new IntType(), new VariableExp(new Variable("y")));
        params.add(param);
        params.add(param1);
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs), 18);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * String text = "Hello World";
     * Foo(int x)
     * 1 + 2;
     * Boolean Baz() {
     * true;
     * }
     * }
     */
    @Test
    public void testParseClassDefTest12() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new StrToken(),
                        new VariableToken("text"), new AssignmentToken(), new StringToken("Hello World"),
                        new SemiColonToken(), new VariableToken("Foo"), new LeftParenToken(),
                        new IntToken(), new VariableToken("x"), new RightParenToken(), new IntegerToken(1),
                        new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new BooleanToken(), new VariableToken("Baz"), new LeftParenToken(), new RightParenToken(),
                        new LeftCurlyToken(), new TrueToken(), new SemiColonToken(),
                        new RightCurlyToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello World"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Param param = new Param(new IntType(), new VariableExp(new Variable("x")));
        params.add(param);
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final List<Param> methodParams = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final Stmt methodStmt = new ExpStmt(new TrueExp());
        stmts.add(methodStmt);
        final Stmt blockStmt = new BlockStmt(stmts);
        final MethodDef MethodDef = new MethodDef(new BoolType(), new MethodName("Baz"), methodParams, blockStmt);
        methoddefs.add(MethodDef);
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 26);
        assertEquals(expected, parser.parseClassdef(0));
    }

    /*
     * class Foo extends Bar {
     * String text = "Hello World";
     * Foo(int x, int y)
     * 1 + 2;
     * Boolean Baz() {
     * true;
     * }
     * }
     */
    @Test
    public void testParseClassDefTest13() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new StrToken(),
                        new VariableToken("text"), new AssignmentToken(), new StringToken("Hello World"),
                        new SemiColonToken(), new VariableToken("Foo"), new LeftParenToken(),
                        new IntToken(), new VariableToken("x"), new RightParenToken(), new IntegerToken(1),
                        new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new BooleanToken(), new VariableToken("Baz"), new LeftParenToken(), new RightParenToken(),
                        new LeftCurlyToken(), new TrueToken(), new SemiColonToken(),
                        new RightCurlyToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final VariableDeclaration vardec = new VariableDeclaration(new StringType(),
                new VariableExp(new Variable("text")), new StringLiteralExp("Hello World"));
        vardecs.add(vardec);
        final List<Param> params = new ArrayList<Param>();
        final Param param = new Param(new IntType(), new VariableExp(new Variable("x")));
        params.add(param);
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final List<Param> methodParams = new ArrayList<Param>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        final Stmt methodStmt = new ExpStmt(new TrueExp());
        stmts.add(methodStmt);
        final Stmt blockStmt = new BlockStmt(stmts);
        final MethodDef MethodDef = new MethodDef(new BoolType(), new MethodName("Baz"), methodParams, blockStmt);
        methoddefs.add(MethodDef);
        final ParseResult<ClassDef> expected = new ParseResult<ClassDef>(
                new ClassDef(classname, new ClassName(""), vardecs, params, stmt, methoddefs), 26);
        assertEquals(expected, parser.parseClassdef(0));

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException1() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"), new RightParenToken(),
                new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException2() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"), new RightParenToken(),
                new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException3() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(), new VariableToken("Bar"),
                        new LeftCurlyToken(), new IntToken(), new LeftParenToken(), new RightParenToken(),
                        new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException4() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(), new VariableToken("Bar"),
                        new LeftCurlyToken(), new IntToken(), new LeftParenToken(), new RightParenToken(),
                        new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                        new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException5() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(),
                new IntToken(), new LeftParenToken(), new RightParenToken(),
                new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(),
                new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException6() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new IntToken(),
                new IntegerToken(2), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException7() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new IntToken(),
                new IntegerToken(2), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException8() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new IntToken(),
                        new IntegerToken(2), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                        new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException9() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new IntegerToken(2),
                new VariableToken("x"), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException10() throws ParserException {
        final Parser parser = new Parser(
                Arrays.asList(new ClassToken(), new VariableToken("Foo"), new LeftCurlyToken(), new IntegerToken(2),
                        new VariableToken("x"), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                        new VariableToken("Foo"), new LeftParenToken(),
                        new LeftParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                        new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException11() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new IntToken(),
                new VariableToken("x"), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                new VariableToken("Foo"), new RightParenToken(),
                new LeftParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(),
                new RightCurlyToken()));
        parser.parseClassdef(0);
    }

    @Test(expected = ParserException.class)
    public void testParseClassDefException12() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new IntegerToken(2),
                new VariableToken("x"), new AssignmentToken(), new IntegerToken(5), new SemiColonToken(),
                new VariableToken("Foo"), new LeftParenToken(),
                new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2),
                new SemiColonToken(), new RightCurlyToken()));
        parser.parseClassdef(0);

    }

    /*
     * class Foo extends Bar {
     * Foo()
     * 1 + 2;
     * }
     */
    @Test
    public void testParseProgram1() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ClassDef theClass = new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs);

        final List<ClassDef> classdefs = new ArrayList<ClassDef>();
        classdefs.add(theClass);
        final List<Stmt> StmtList = new ArrayList<Stmt>();
        final ParseResult<Program> expected = new ParseResult<Program>(new Program(classdefs, StmtList), 13);
        assertEquals(expected, parser.parseProgram(0));
    }

    @Test
    public void testParseProgram2() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ClassDef theClass = new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs);
        final List<ClassDef> classdefs = new ArrayList<ClassDef>();
        classdefs.add(theClass);
        final List<Stmt> StmtList = new ArrayList<Stmt>();
        final ParseResult<Program> expected = new ParseResult<Program>(new Program(classdefs, StmtList), 13);
        assertEquals(expected, parser.parseProgram(0));
    }

    @Test(expected = ParserException.class)
    public void testParseProgramException1() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new LessThanToken(), new LessThanToken(), new LessThanToken()));
        parser.parseProgram();
    }

    /*
     * class Foo extends Bar {
     * Foo()
     * 1 + 2;
     * }
     * println();
     * 
     */

    @Test
    public void testParseProgram3() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken(),
                new PrintlnToken(), new LeftParenToken(), new RightParenToken(), new SemiColonToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ClassDef theClass = new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs);
        final List<Exp> exps = new ArrayList<Exp>();
        final Stmt theStmt = new PrintlnStmt(exps);
        final List<ClassDef> classdefs = new ArrayList<ClassDef>();
        classdefs.add(theClass);
        final List<Stmt> StmtList = new ArrayList<Stmt>();
        StmtList.add(theStmt);
        final ParseResult<Program> expected = new ParseResult<Program>(new Program(classdefs, StmtList), 17);
        assertEquals(expected, parser.parseProgram(0));
    }

    @Test
    public void testParseProgram4() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(), new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
														new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(), new IntegerToken(2), new SemiColonToken(), new RightCurlyToken(),
														new PrintlnToken(), new LeftParenToken(), new RightParenToken(), new SemiColonToken()));
		final ClassName classname = new ClassName("Foo");
		final ClassName extendsClassname = new ClassName("Bar");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Param> params = new ArrayList<Param>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
		final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
		final ClassDef theClass = new ClassDef(classname, extendsClassname, vardecs, params, stmt, methoddefs);
		final List<Exp> exps = new ArrayList<Exp>();
		final Stmt theStmt = new PrintlnStmt(exps);
		final List<ClassDef> classdefs = new ArrayList<ClassDef>();
		classdefs.add(theClass);
		final List<Stmt> StmtList = new ArrayList<Stmt>();
		StmtList.add(theStmt);
		final ParseResult<Program> expected = new ParseResult<Program>(new Program(classdefs, StmtList), 17);
		assertEquals(expected.result, parser.parseProgram());
    }

    /*

        * class Foo extends Bar {
        * Foo()
        * 1 + 2;
        * }
        * println("Hello");
        * 

    */
    @Test
    public void testParseProgram5() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Foo"), new ExtendsToken(),
                new VariableToken("Bar"), new LeftCurlyToken(), new VariableToken("Foo"),
                new LeftParenToken(), new RightParenToken(), new IntegerToken(1), new AdditionToken(),
                new IntegerToken(2), new SemiColonToken(), new RightCurlyToken(),
                new PrintlnToken(), new LeftParenToken(), new StringToken("Hello"), new RightParenToken(),
                new SemiColonToken()));
        final ClassName classname = new ClassName("Foo");
        final ClassName extendsClassName = new ClassName("Bar");
        final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
        final List<Param> params = new ArrayList<Param>();
        final Stmt stmt = new ExpStmt(new OpExp(new IntegerLiteralExp(1), new PlusOp(), new IntegerLiteralExp(2)));
        final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
        final ClassDef theClass = new ClassDef(classname, extendsClassName, vardecs, params, stmt, methoddefs);
        final List<Exp> exps = new ArrayList<Exp>();
        exps.add(new StringLiteralExp("Hello"));
        final Stmt theStmt = new PrintlnStmt(exps);
        final List<ClassDef> classdefs = new ArrayList<ClassDef>();
        classdefs.add(theClass);
        final List<Stmt> StmtList = new ArrayList<Stmt>();
        StmtList.add(theStmt);
        final ParseResult<Program> expected = new ParseResult<Program>(new Program(classdefs, StmtList), 18);
        assertEquals(expected, parser.parseProgram(0));
    }

    /*
    Failed tests:   testParseProgram4(parser.ParserTest): expected:
    <ParseResult(Program([ClassDef(ClassName(Foo), ClassName(Bar), [], [], [], ExpStmt(OpExp(IntegerExp(1), PlusOp, IntegerExp(2))))], [println([])]), 18)> 
    
    but was:
    
    <ParseResult(Program([ClassDef(ClassName(Foo), ClassName(Bar), [], [], [], ExpStmt(OpExp(IntegerExp(1), PlusOp, IntegerExp(2))))], [println([StringExp(Hello)])]), 18)>
    /
    */
    /*
    Failed tests:   testParseProgram4(parser.ParserTest): expected:
    <ParseResult(Program([ClassDef(ClassName(Foo), ClassName(Bar), [], [], [], ExpStmt(OpExp(IntegerExp(1), PlusOp, IntegerExp(2))))], [println([])]), 22)> 
    
    but was:
    
    <ParseResult(Program([ClassDef(ClassName(Foo), ClassName(Bar), [], [], [], ExpStmt(OpExp(IntegerExp(1), PlusOp, IntegerExp(2))))], [println([StringExp(Hello)])]), 18)>
    */
   


    /*
     * // // foo.bar()
     * // @Test
     * // public void testVarMethodCall() throws ParserException {
     * // final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"),
     * new
     * // PeriodToken(),
     * // new VariableToken("bar"), new LeftParenToken(), new RightParenToken()));
     * // final Exp variable = new VariableExp(new Variable("foo"));
     * // final Exp name = new VariableExp(new Variable("bar"));
     * // final List<Exp> inside = new ArrayList<>();
     * // assertEquals(new ParseResult<Exp>(new VarMethodNameCallExp(variable, name,
     * // inside), 5),
     * // parser.parseVarMethodNameCallExp(0));
     * // }
     * 
     * // @Test
     * // public void testVarAssignExp() throws ParserException {
     * // final Parser parser = new Parser(Arrays.asList(new IntToken(), new
     * // VariableToken("foo"), new AssignmentToken(),
     * // new IntegerToken(5), new SemiColonToken()));
     * // final Type type = new IntType();
     * 
     * // final Exp variable = new VariableExp(new Variable("foo"));
     * // final Exp value = new IntegerLiteralExp(5);
     * // assertEquals(new ParseResult<VarDec>(new VariableDeclaration(type,
     * variable,
     * // value), 5), parser.parseVarDec(0));
     * 
     * // }
     * 
     * /*
     * 
     * @Test
     * public void testWhileExp() throws ParserException {
     * final Parser parser = new Parser(Arrays.asList(new WhileToken(), new
     * LeftParenToken(), new VariableToken("foo"),
     * new LessThanToken(), new IntegerToken(5),
     * new RightParenToken(), new VariableToken("foo"), new AssignmentToken(), new
     * IntegerToken(1),
     * new SemiColonToken()));
     * final ParseResult<Exp> guard = new ParseResult<Exp>(
     * new OpExp(new VariableExp(new Variable("foo")), new LessThanOp(), new
     * IntegerLiteralExp(5)), 3);
     * final ParseResult<Stmt> stmt = new ParseResult<Stmt>(
     * new VarAssignStmt(new VariableExp(new Variable("foo")), new
     * IntegerLiteralExp(1)), 4);
     * final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
     * WhileStmt(guard.result, stmt.result), 10);
     * assertEquals(expected, parser.parseStmt(0));
     * 
     * }
     * 
     * @Test
     * public void testBreakStmt() throws ParserException{
     * final Parser parser = new Parser(Arrays.asList(new BreakToken(), new
     * SemiColonToken()));
     * final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
     * BreakStmt("break",";" ),2);
     * assertEquals(expected, parser.parseBreakStmt(0));
     * 
     * }
     */

    // @Test
    // public void testIfElse() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new IfToken(), new
    // LeftParenToken(), new VariableToken("foo"), new GreaterThanToken(), new
    // IntegerToken(1), new RightParenToken(), new VariableToken("bar"),
    // new AssignmentToken(), new IntegerToken(0), new SemiColonToken(), new
    // ElseToken(), new VariableToken("foo"), new AssignmentToken(), new
    // IntegerToken(1), new SemiColonToken()));
    // final ParseResult<Exp> ifGuard = new ParseResult<Exp>(new OpExp(new
    // VariableExp(new Variable("foo")), new GreaterThanOp(), new
    // IntegerLiteralExp(1)), 3);
    // final ParseResult<Stmt> trueBranch = new ParseResult<Stmt>(new
    // VarAssignStmt(new VariableExp(new Variable("bar")), new
    // IntegerLiteralExp(0)), 4);
    // final ParseResult<Stmt> falseBranch = new ParseResult<Stmt>(new
    // VarAssignStmt(new VariableExp(new Variable("foo")), new
    // IntegerLiteralExp(1)), 4);
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
    // IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), 15);
    // assertEquals(expected, parser.parseStmt(0));
    // }

    // @Test
    // public void testWhileStmt() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new WhileToken(), new
    // LeftParenToken(), new VariableToken("foo"), new LessThanToken(), new
    // IntegerToken(5),
    // new RightParenToken(), new VariableToken("foo"), new AssignmentToken(), new
    // IntegerToken(1), new SemiColonToken()));
    // final ParseResult<Exp> guard = new ParseResult<Exp>(new OpExp(new
    // VariableExp(new Variable("foo")), new LessThanOp(), new
    // IntegerLiteralExp(5)), 3);
    // final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new VarAssignStmt(new
    // VariableExp(new Variable("foo")), new IntegerLiteralExp(1)), 4);
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
    // WhileStmt(guard.result, stmt.result), 10);
    // assertEquals(expected, parser.parseStmt(0));
    // }

    // @Test
    // public void testReturnStmt() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new ReturnToken(), new
    // VariableToken("y"), new SemiColonToken()));
    // final ParseResult<Exp> exp = new ParseResult<Exp>(new VariableExp(new
    // Variable("y")), 1);
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
    // ReturnStmt(exp.result), 3);
    // assertEquals(expected, parser.parseStmt(0));
    // }

    // @Test
    // public void testVarAssignStmt() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new
    // AssignmentToken(), new IntegerToken(10),
    // new SemiColonToken()));
    // final ParseResult<Exp> variable = new ParseResult<Exp>(new VariableExp(new
    // Variable("foo")), 1);
    // final ParseResult<Exp> value = new ParseResult<Exp>(new
    // IntegerLiteralExp(10), 1);
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
    // VarAssignStmt(variable.result, value.result), 4);
    // assertEquals(expected, parser.parseStmt(0));
    // }

    // @Test
    // public void testParseThisStmt() throws ParserException {
    // final Parser parser= new Parser(Arrays.asList(new ThisToken(), new
    // PeriodToken(), new VariableToken("foo"), new AssignmentToken(),new
    // VariableToken("bar"), new SemiColonToken()));
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ThisStmt(new
    // VariableExp(new Variable("foo")), new VariableExp(new Variable("bar"))),6 );
    // assertEquals(expected,parser.parseThisStmt(0));
    // }

    // @Test
    // public void testBlockStmtSingle() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new LeftCurlyToken(), new
    // VariableToken("foo"), new AssignmentToken(), new VariableToken("bar"), new
    // SemiColonToken(), new RightCurlyToken()));
    // final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new VarAssignStmt(new
    // VariableExp(new Variable("foo")), new VariableExp(new Variable("bar"))), 3);
    // final List<Stmt> stmts = new ArrayList<Stmt>();
    // stmts.add(stmt1.result);
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new
    // BlockStmt(stmts), 6);
    // assertEquals(expected, parser.parseStmt(0));
    // }

    // bool b = true;
    // @Test
    // public void testParseVarDecBool() throws ParserException {
    // final Parser parser = new Parser(Arrays.asList(new VariableToken("b"), new
    // AssignmentToken(), new TrueToken(), new SemiColonToken()));
    // final ParseResult<Stmt> expected = new ParseResult<Stmt>(new VarDecStmt(new
    // vardec), 1);
    // assertEquals(expected, parser.parseStmt(0));

    // }

}

/*
 * @Test
 * public void testEqualsOpExp() {
 * final OpExp first = new OpExp(new IntegerLiteralExp(1),
 * new PlusOp(),
 * new IntegerLiteralExp(1));
 * final OpExp second = new OpExp(new IntegerLiteralExp(1),
 * new PlusOp(),
 * new IntegerLiteralExp(1));
 * assertEquals(first, second);
 * }
 * 
 * @Test
 * public void testDoubleEqualsOp() throws ParserException {
 * final OpExp first = new OpExp(new IntegerLiteralExp(1),
 * new DoubleEqualsOp(),
 * new IntegerLiteralExp(1));
 * final OpExp second = new OpExp(new IntegerLiteralExp(1),
 * new DoubleEqualsOp(),
 * new IntegerLiteralExp(1));
 * assertEquals(first, second);
 * }
 * 
 * @Test
 * public void testNotEqualsOp() throws ParserException {
 * final OpExp first = new OpExp(new IntegerLiteralExp(1),
 * new NotEqualsOp(),
 * new IntegerLiteralExp(1));
 * final OpExp second = new OpExp(new IntegerLiteralExp(1),
 * new NotEqualsOp(),
 * new IntegerLiteralExp(1));
 * assertEquals(first, second);
 * }
 * 
 * @Test
 * public void testPrimaryVariable() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
 * assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")),
 * 1),
 * parser.parsePrimaryExp(0));
 * }
 * 
 * @Test
 * public void testPrimaryInteger() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
 * assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
 * parser.parsePrimaryExp(0));
 * }
 * 
 * 
 * @Test
 * public void testPrimaryParens() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new LeftParenToken(),
 * new IntegerToken(123),
 * new RightParenToken()));
 * assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 3),
 * parser.parsePrimaryExp(0));
 * }
 * 
 * @Test
 * public void testAdditiveOpPlus() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new AdditionToken()));
 * assertEquals(new ParseResult<Op>(new PlusOp(), 1),
 * parser.parseAdditiveOp(0));
 * }
 * 
 * @Test //test failing
 * public void testAdditiveOpMinus() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new SubtractionToken()));
 * assertEquals(new ParseResult<Op>(new SubtractionOp(), 1),
 * parser.parseAdditiveOp(0));
 * }
 * 
 * @Test
 * public void testAdditiveOpDivide() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new DivisionToken()));
 * assertEquals(new ParseResult<Op>(new DivisionOp(), 1),
 * parser.parseAdditiveOp(0));
 * }
 * 
 * @Test
 * public void testAdditiveOpMultiply() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
 * assertEquals(new ParseResult<Op>(new MultiplicationOp(), 1),
 * parser.parseAdditiveOp(0));
 * }
 * 
 * @Test
 * public void testAdditiveExpOnlyPrimary() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
 * assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
 * parser.parseAdditiveExp(0));
 * }
 * 
 * @Test
 * public void testAdditiveExpSingleOperator() throws ParserException {
 * // 1 + 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new AdditionToken(),
 * new IntegerToken(2)));
 * assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
 * new PlusOp(),
 * new IntegerLiteralExp(2)),
 * 3),
 * parser.parseAdditiveExp(0));
 * }
 * 
 * @Test
 * public void testAdditiveExpMultiOperator() throws ParserException {
 * // 1 + 2 - 3 ==> (1 + 2) - 3
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new AdditionToken(),
 * new IntegerToken(2),
 * new SubtractionToken(),
 * new IntegerToken(3)));
 * final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
 * new PlusOp(),
 * new IntegerLiteralExp(2)),
 * new SubtractionOp(),
 * new IntegerLiteralExp(3));
 * assertEquals(new ParseResult<Exp>(expected, 5),
 * parser.parseAdditiveExp(0));
 * }
 * 
 * @Test
 * public void testLessThanExpOnlyAdditive() throws ParserException {
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
 * assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
 * parser.parseLessThanExp(0));
 * }
 * 
 * @Test
 * public void testLessThanSingleOperator() throws ParserException {
 * // 1 < 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new LessThanToken(),
 * new IntegerToken(2)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new LessThanOp(),
 * new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Exp>(expected, 3),
 * parser.parseLessThanExp(0));
 * }
 * 
 * @Test
 * public void testLessThanMultiOperator() throws ParserException {
 * // 1 < 2 < 3 ==> (1 < 2) < 3
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new LessThanToken(),
 * new IntegerToken(2),
 * new LessThanToken(),
 * new IntegerToken(3)));
 * final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
 * new LessThanOp(),
 * new IntegerLiteralExp(2)),
 * new LessThanOp(),
 * new IntegerLiteralExp(3));
 * assertEquals(new ParseResult<Exp>(expected, 5),
 * parser.parseLessThanExp(0));
 * }
 * 
 * @Test
 * public void testLessThanMixedOperator() throws ParserException {
 * // 1 < 2 + 3 ==> 1 < (2 + 3)
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new LessThanToken(),
 * new IntegerToken(2),
 * new AdditionToken(),
 * new IntegerToken(3)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new LessThanOp(),
 * new OpExp(new IntegerLiteralExp(2),
 * new PlusOp(),
 * new IntegerLiteralExp(3)));
 * assertEquals(new ParseResult<Exp>(expected, 5),
 * parser.parseLessThanExp(0));
 * }
 * 
 * @Test
 * public void testGreaterThanSingleOperator() throws ParserException {
 * // 1 > 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new GreaterThanToken(),
 * new IntegerToken(2)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new GreaterThanOp(),
 * new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Exp>(expected, 3),
 * parser.parseGreaterThanExp(0));
 * }
 * 
 * @Test
 * public void testGreaterThanMultiOperator() throws ParserException {
 * // 1 > 2 > 3 ==> (1 > 2) > 3
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new GreaterThanToken(),
 * new IntegerToken(2),
 * new GreaterThanToken(),
 * new IntegerToken(3)));
 * final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
 * new GreaterThanOp(),
 * new IntegerLiteralExp(2)),
 * new GreaterThanOp(),
 * new IntegerLiteralExp(3));
 * assertEquals(new ParseResult<Exp>(expected, 5),
 * parser.parseGreaterThanExp(0));
 * }
 * 
 * @Test
 * public void testDoubleEqualsOperator() throws ParserException {
 * // 1 == 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new EqualsToken(),
 * new IntegerToken(2)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new DoubleEqualsOp(),
 * new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Exp>(expected, 3),
 * parser.parseEqualsExp(0));
 * }
 * 
 * @Test
 * public void testEqualsOperator() throws ParserException {
 * // 1 = 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new AssignmentToken(),
 * new IntegerToken(2)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new EqualsOp(),
 * new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Exp>(expected, 3),
 * parser.parseEqualsExp(0));
 * }
 * 
 * @Test
 * public void testPeriodOperator() throws ParserException {
 * // 1 . 2
 * final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
 * new PeriodToken(),
 * new IntegerToken(2)));
 * final Exp expected = new OpExp(new IntegerLiteralExp(1),
 * new PeriodOp(),
 * new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Exp>(expected, 3),
 * parser.parseDotExp(0));
 * }
 * 
 * @Test //fails ----- wait now it works!
 * public void testPrintlnParse() throws ParserException {
 * 
 * final Parser parser = new Parser(Arrays.asList(new PrintlnToken(),
 * new LeftParenToken(), new IntegerToken(2), new RightParenToken(), new
 * SemiColonToken()));
 * final PrintlnStmt expected = new PrintlnStmt(new IntegerLiteralExp(2));
 * assertEquals(new ParseResult<Stmt>(expected, 5),
 * parser.parseStmt(0));
 * 
 * }
 * 
 */
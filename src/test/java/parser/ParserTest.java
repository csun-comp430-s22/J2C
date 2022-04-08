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
	public void testTypeVoid() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VoidToken()));
        assertEquals(new ParseResult<Type>(new VoidType(), 1), parser.parseType(0));

    }
    @Test
	public void testTypeVariableClass() throws ParserException {
        final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
        assertEquals(new ParseResult<Type>(new ClassType(new ClassName("x")), 1), parser.parseType(0));

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

    @Test
	public void testParseExpForNewClassExp() throws ParserException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new LeftParenToken(),
				new IntegerToken(22), new RightParenToken()));
		List<Exp> inside = new ArrayList<>();
		inside.add(new IntegerLiteralExp(22));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
          new NewClassNameExp(new VariableExp(new Variable("Dog")), inside), 5);
          assertEquals(expected, parser.parseNewClassExp(0));


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

    //foo.bar()
	@Test
	public void testVarMethodCall() throws ParserException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("foo"), new PeriodToken(),
				new VariableToken("bar"), new LeftParenToken(), new RightParenToken()));
		final Exp variable = new VariableExp(new Variable("foo"));
		final Exp name = new VariableExp(new Variable("bar"));
		final List<Exp> inside = new ArrayList<>();
		assertEquals(new ParseResult<Exp>(new VarMethodNameCallExp(variable, name, inside), 5), parser.parseVarMethodNameCallExp(0));
	}

    @Test
	public void testparseVardec() throws ParserException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new AssignmentToken(),
				new IntegerToken(3), new SemiColonToken()));
		final Type type = new IntType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new IntegerLiteralExp(3);
		assertEquals(new ParseResult<VarDec>(new VariableDeclaration(type, variable, exp), 5), parser.parseVarDec(0));
		;
	}
   



}


/*
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

    @Test
    public void testDoubleEqualsOperator() throws ParserException {
        // 1 == 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new EqualsToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new DoubleEqualsOp(),
                                       new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseEqualsExp(0));
    }

    @Test
    public void testEqualsOperator() throws ParserException {
        // 1 = 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new AssignmentToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new EqualsOp(),
                                       new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseEqualsExp(0));
    }
    @Test
    public void testPeriodOperator() throws ParserException {
        // 1 . 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new PeriodToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                       new PeriodOp(),
                                       new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseDotExp(0));
    }
    @Test //fails ----- wait now it works!  
    public void testPrintlnParse() throws ParserException {
   
        final Parser parser = new Parser(Arrays.asList(new PrintlnToken(),
                                                       new LeftParenToken(), new IntegerToken(2), new RightParenToken(), new SemiColonToken()));
        final PrintlnStmt expected = new PrintlnStmt(new IntegerLiteralExp(2));
        assertEquals(new ParseResult<Stmt>(expected, 5),
                     parser.parseStmt(0));
        
    }

    */
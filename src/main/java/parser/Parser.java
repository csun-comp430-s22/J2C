package parser;

import lexer.*;

import java.util.List;

import java.util.ArrayList;

public class Parser {
    private final List<Token> tokens;

    public Parser(final List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token getToken(final int position) throws ParserException {
        if (position >= 0 && position < tokens.size()) {
            return tokens.get(position);
        } else {
            throw new ParserException("Invalid token position: " + position);
        }
    }

    // public ParseResult<Stmt> parse() throws ParserException {

    // }

    // public ParseResult<Op> parseOp(final int position) throws ParserException {
    // final Token token = getToken(position);
    // if (token instanceof AdditionToken) {
    // return new ParseResult<Op>(new PlusOp(), position + 1);
    // } else if (token instanceof SubtractionToken) {
    // return new ParseResult<Op>(new SubtractionOp(), position + 1);
    // } else if (token instanceof LessThanToken) {
    // return new ParseResult<Op>(new LessThanOp(), position + 1);
    // } else if (token instanceof AssignmentToken) {
    // return new ParseResult<Op>(new EqualsOp(), position + 1);
    // } else if(token instanceof GreaterThanToken) {
    // return new ParseResult<Op>(new GreaterThanOp(), position + 1);
    // } else if(token instanceof MultiplicationToken) {
    // return new ParseResult<Op>(new MultiplicationOp(), position + 1);
    // } else if(token instanceof DivisionToken) {
    // return new ParseResult<Op>(new DivisionOp(), position + 1);
    // } else {
    // throw new ParserException("Expected operator; Received: " + token);
    // }
    // }

    public void assertTokenHereIs(final int position, final Token expected) throws ParserException {
        final Token received = getToken(position);
        if (!expected.equals(received)) {
            throw new ParserException("expected: " + expected + "; received: " + received);
        }
    }

    public ParseResult<Type> parseType(int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof IntToken) {
            return new ParseResult<Type>(new IntType(), position + 1);
        } else if (token instanceof BooleanToken) {
            return new ParseResult<Type>(new BoolType(), position + 1);
        } else if (token instanceof StrToken) {
            return new ParseResult<Type>(new StringType(), position + 1);
        } else if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Type>(new ClassNameType(new ClassName(name)),
                    position + 1);
        } else {
            throw new ParserException("Expected type; Received: " + token);
        }
    }

    public ParseResult<Exp> parsePrimaryExp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Exp>(new VariableExp(new Variable(name)), position + 1);
        } else if (token instanceof StringToken) {
            final String value = ((StringToken) token).value;
            return new ParseResult<Exp>(new StringLiteralExp(value), position + 1);
        } else if (token instanceof IntegerToken) {
            final int value = ((IntegerToken) token).value;
            return new ParseResult<Exp>(new IntegerLiteralExp(value), position + 1);
        } else if (token instanceof TrueToken) {
            return new ParseResult<Exp>(new TrueExp(), position + 1);
        } else if (token instanceof FalseToken) {
            return new ParseResult<Exp>(new FalseExp(), position + 1);
        } else {
            throw new ParserException("Expected primary expression; Received: " + token);
        }
    }

    public ParseResult<Op> parseMultiplicativeOp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof MultiplicationToken) {
            return new ParseResult<Op>(new MultiplicationOp(), position + 1);
        } else if (token instanceof DivisionToken) {
            return new ParseResult<Op>(new DivisionOp(), position + 1);
        } else {
            throw new ParserException("Expected multiplicative operator; Received: " + token);
        }
    }

    public ParseResult<Exp> parseMultiplicativeExp(final int position) throws ParserException {
        ParseResult<Exp> current = parsePrimaryExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                final ParseResult<Op> op = parseMultiplicativeOp(current.position);
                final ParseResult<Exp> next = parsePrimaryExp(op.position);
                current = new ParseResult<Exp>(new OpExp(current.result, op.result, next.result), next.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
        }
        return current;

    }

    public ParseResult<Op> parseAdditiveOp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof AdditionToken) {
            return new ParseResult<Op>(new PlusOp(), position + 1);
        } else if (token instanceof SubtractionToken) {
            return new ParseResult<Op>(new SubtractionOp(), position + 1);
        } else {
            throw new ParserException("Expected additive operator; Received: " + token);
        }
    }

    public ParseResult<Exp> parseAdditiveExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseMultiplicativeExp(position);
        boolean shouldRun = true;
        while (shouldRun) {
            try {
                final ParseResult<Op> op = parseAdditiveOp(current.position);
                final ParseResult<Exp> next = parseMultiplicativeExp(op.position);
                current = new ParseResult<Exp>(new OpExp(current.result, op.result, next.result), next.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
        }
        return current;
    }

    public ParseResult<Op> parseComparisonOp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof LessThanToken) {
            return new ParseResult<Op>(new LessThanOp(), position + 1);
        } else if (token instanceof GreaterThanToken) {
            return new ParseResult<Op>(new GreaterThanOp(), position + 1);
        } else if (token instanceof EqualsToken) {
            return new ParseResult<Op>(new EqualsOp(), position + 1);
        } else if (token instanceof NotEqualsToken) {
            return new ParseResult<Op>(new NotEqualsOp(), position + 1);
        } else {
            throw new ParserException("Expected comparison operator; Received: " + token);
        }
    }

    public ParseResult<Exp> parseComparisonExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseAdditiveExp(position);
        try {
            final Token token = getToken(position + 1);
            if ((token instanceof LessThanToken) || (token instanceof GreaterThanToken)
                    || (token instanceof EqualsToken) || (token instanceof NotEqualsToken)) {
                final ParseResult<Op> op = parseComparisonOp(current.position);
                final ParseResult<Exp> next = parseAdditiveExp(op.position);
                current = new ParseResult<Exp>(new OpExp(current.result, op.result, next.result), next.position);
            } else {
                return current;
            }
        } catch (final ParserException e) {
            return current;
        }
        return current;
    }

    public ParseResult<ClassNameExp> parseClassNameExp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<ClassNameExp>(new ClassNameExp(new ClassName(name)), position + 1);
        } else {
            throw new ParserException("Expected class name; Received: " + token);
        }
    }

    public ParseResult<Exp> parseNewClassNameExp(final int position) throws ParserException {
        Token token = getToken(position + 1);
        final String name = ((VariableToken) token).name;
        assertTokenHereIs(position + 1, new VariableToken(name));
        final ParseResult<ClassNameExp> classNameExp = parseClassNameExp(position + 1);
        assertTokenHereIs(classNameExp.position, new LeftParenToken());
        List<Exp> arguments = new ArrayList<Exp>();
        int currentPosition = classNameExp.position + 1;
        token = getToken(currentPosition);
        final ParseResult<Exp> result;
        if (token instanceof RightParenToken) {
            result = new ParseResult<Exp>(new NewClassNameExp(classNameExp.result, arguments), currentPosition + 1);
        } else {
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    final ParseResult<Exp> argument = parseExp(currentPosition);
                    arguments.add(argument.result);
                    Token nextToken = getToken(argument.position);
                    if (nextToken instanceof CommaToken) {
                        currentPosition = argument.position + 1;
                    } else {
                        currentPosition = argument.position;
                    }
                } catch (final ParserException e) {
                    shouldRun = false;
                }
            }
            assertTokenHereIs(currentPosition, new RightParenToken());
            return new ParseResult<Exp>(new NewClassNameExp(classNameExp.result, arguments), currentPosition + 1);
        }
        return result;
    }

    public ParseResult<MethodNameExp> parseMethodName(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<MethodNameExp>(new MethodNameExp(new MethodName(name)), position + 1);
        } else {
            throw new ParserException("Expected method name; Received: " + token);
        }
    }

    public ParseResult<Exp> parseVarMethodNameCall(final int position) throws ParserException {
        ParseResult<Exp> current = parsePrimaryExp(position);
        final Token token = getToken(position + 2);
        final String name = ((VariableToken) token).name;
        assertTokenHereIs(position + 2, new VariableToken(name));
        ParseResult<MethodNameExp> methodNameExp = parseMethodName(position + 2);
        assertTokenHereIs(position + 3, new LeftParenToken());
        List<Exp> arguments = new ArrayList<Exp>();
        Token nextToken = getToken(position + 4);
        if (nextToken instanceof RightParenToken) {
            return new ParseResult<Exp>(new VarMethodCallExp(current.result, methodNameExp.result, arguments),
                    position + 5);
        } else if ((nextToken instanceof VariableToken || nextToken instanceof IntegerToken
                || nextToken instanceof StringToken || nextToken instanceof TrueToken
                || nextToken instanceof FalseToken)) {
            ParseResult<Exp> argument = parsePrimaryExp(position + 4);
            arguments.add(argument.result);
            int i = position + 5;
            nextToken = getToken(i);
            if (!(nextToken instanceof RightParenToken)) {
                Token commaToken = getToken(position + 5);
                i = position + 6;
                nextToken = getToken(i);
                while ((commaToken instanceof CommaToken) && ((nextToken instanceof VariableToken)
                        || (nextToken instanceof IntegerToken) || (nextToken instanceof StringToken)
                        || (nextToken instanceof TrueToken) || (nextToken instanceof FalseToken))) {
                    ParseResult<Exp> argument2 = parsePrimaryExp(i);
                    arguments.add(argument2.result);
                    i = i + 1;
                    nextToken = getToken(i);
                    if (!(nextToken instanceof RightParenToken)) {
                        // need to test
                        commaToken = getToken(i);
                        i = i + 1;
                        nextToken = getToken(i);
                    }
                }
            }
            assertTokenHereIs(i, new RightParenToken());
            return new ParseResult<Exp>(new VarMethodCallExp(current.result, methodNameExp.result, arguments), i + 1);

        } else {
            throw new ParserException("Expected method call arguments; Received: " + nextToken);
        }

    }

    public ParseResult<Exp> parseExp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            try {
                assertTokenHereIs(position + 1, new PeriodToken());
                return parseVarMethodNameCall(position);
            } catch (final ParserException e) {
                return parseComparisonExp(position);
            }
        } else if (token instanceof NewToken) {
            return parseNewClassNameExp(position);
        } else {
            return parseComparisonExp(position);
        }
    }

    public ParseResult<VariableDeclaration> parseVarDec(final int position) throws ParserException {
        final Token token = getToken(position);
        if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StrToken)
                || (token instanceof VariableToken)) {
            final Token nextToken = getToken(position + 1);
            final String name = ((VariableToken) nextToken).name;
            assertTokenHereIs(position + 1, new VariableToken(name));
            final Token nextNextToken = getToken(position + 2);
            if (nextNextToken instanceof AssignmentToken) {
                final ParseResult<Type> type = parseType(position);
                final ParseResult<Exp> variable = parsePrimaryExp(position + 1);
                final ParseResult<Exp> exp = parseExp(position + 3);
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<VariableDeclaration>(
                        new VariableDeclaration(type.result, variable.result, exp.result),
                        exp.position + 1);
            } else {
                throw new ParserException("Expected assignment token; Received: " + nextNextToken);
            }
        } else {
            throw new ParserException("Expected variable token; Received: " + token);
        }
    }

    public ParseResult<Param> parseParam(final int position) throws ParserException {
        final Token token = getToken(position);
        if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StrToken)
                || (token instanceof VariableToken)) {
            final ParseResult<Type> type = parseType(position);
            final Token nextToken = getToken(position + 1);
            final String name = ((VariableToken) nextToken).name;
            assertTokenHereIs(position + 1, new VariableToken(name));
            final ParseResult<Exp> exp = parsePrimaryExp(position + 1);
            return new ParseResult<Param>(new Param(type.result, exp.result), position + 2);
        } else {
            throw new ParserException("Expected variable token; Received: " + token);
        }
    }

    public ParseResult<Stmt> parseBreakStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        final Token nextToken = getToken(position + 1);
        final String breakToken = ((BreakToken) token).toString();
        assertTokenHereIs(position + 1, new SemiColonToken());
        final String stringToken = ((SemiColonToken) nextToken).toString();
        return new ParseResult<Stmt>(new BreakStmt(breakToken, stringToken), position + 2);
    }

    public ParseResult<Stmt> parseSuperStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        assertTokenHereIs(position + 1, new LeftParenToken());
        final Token nextToken = getToken(position + 2);
        final String var = ((VariableToken) nextToken).name;
        assertTokenHereIs(position + 2, new VariableToken(var));
        assertTokenHereIs(position + 3, new RightParenToken());
        assertTokenHereIs(position + 4, new SemiColonToken());
        return new ParseResult<Stmt>(new SuperStmt(((SuperToken) token).toString(), new VariableExp(new Variable(var))),
                position + 5);
    }

    public ParseResult<Stmt> parseThisStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        assertTokenHereIs(position + 1, new PeriodToken());
        final Token nextToken = getToken(position + 2);
        final String var = ((VariableToken) nextToken).name;
        assertTokenHereIs(position + 2, new VariableToken(var));
        assertTokenHereIs(position + 3, new AssignmentToken());
        final Token nextNextToken = getToken(position + 4);
        final String var2 = ((VariableToken) nextNextToken).name;
        assertTokenHereIs(position + 4, new VariableToken(var2));
        assertTokenHereIs(position + 5, new SemiColonToken());
        return new ParseResult<Stmt>(
                new ThisStmt(new VariableExp(new Variable(var)), new VariableExp(new Variable(var2))), position + 6);
    }

    public ParseResult<Stmt> parseStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        if ((token instanceof VariableToken) && (getToken(position + 1) instanceof AssignmentToken)) {
            final ParseResult<Exp> variable = parsePrimaryExp(position);
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new SemiColonToken());
            return new ParseResult<Stmt>(new VarAssignStmt(variable.result, exp.result), exp.position + 1);
        } else if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StrToken)
                || (token instanceof VariableToken)) {
            final Token nextToken = getToken(position + 1);
            if (nextToken instanceof VariableToken) {
                final String nextTokenName = ((VariableToken) nextToken).name;
                assertTokenHereIs(position + 1, new VariableToken(nextTokenName));
                final ParseResult<VariableDeclaration> declare = parseVarDec(position);
                return new ParseResult<Stmt>(new VarDecStmt(declare), declare.position);
            } else {
                final ParseResult<Exp> exp = parseExp(position);
                final ParseResult<Stmt> expStmt = new ParseResult<Stmt>(new ExpStmt(exp.result), exp.position + 1);
                return expStmt;
            }
        } else if (token instanceof WhileToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> guard = parseExp(position + 2);
            assertTokenHereIs(guard.position, new RightParenToken());
            final ParseResult<Stmt> whileStmt = parseStmt(guard.position + 1);
            return new ParseResult<Stmt>(new WhileStmt(guard.result, whileStmt.result), whileStmt.position);
        } else if (token instanceof BreakToken) {
            final ParseResult<Stmt> breakResult;
            breakResult = parseBreakStmt(position);
            return breakResult;
        } else if (token instanceof IfToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> ifGuard = parseExp(position + 2);
            assertTokenHereIs(ifGuard.position, new RightParenToken());
            final ParseResult<Stmt> trueBranch = parseStmt(ifGuard.position + 1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
            return new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result),
                    falseBranch.position);
        } else if (token instanceof ReturnToken) {
            final ParseResult<Exp> retExp = parseExp(position + 1);
            assertTokenHereIs(retExp.position, new SemiColonToken());
            return new ParseResult<Stmt>(new ReturnStmt(retExp.result), retExp.position + 1);
        } else if (token instanceof LeftCurlyToken) {
            List<Stmt> stmts = new ArrayList<Stmt>();
            Token nextToken = getToken(position + 1);
            if (nextToken instanceof RightCurlyToken) {
                return new ParseResult<Stmt>(new BlockStmt(stmts), position + 2);
            } else {
                ParseResult<Stmt> stmt1 = parseStmt(position + 1);
                stmts.add(stmt1.result);
                int count = stmt1.position;
                Token theNextToken = getToken(count);
                while (!(theNextToken instanceof RightCurlyToken)) {
                    ParseResult<Stmt> stmt2 = parseStmt(count);
                    stmts.add(stmt2.result);
                    count = stmt2.position;
                    theNextToken = getToken(count);
                }
                assertTokenHereIs(count, new RightCurlyToken());
                return new ParseResult<Stmt>(new BlockStmt(stmts), count + 1);
            }
        } else if (token instanceof PrintlnToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            List<Exp> exps = new ArrayList<Exp>();
            Token theNextToken = getToken(position + 2);
            if (theNextToken instanceof RightParenToken) {
                assertTokenHereIs(position + 3, new SemiColonToken());
                return new ParseResult<Stmt>(new PrintlnStmt(exps), position + 4);
            } else {
                ParseResult<Exp> exp1 = parseExp(position + 2);
                exps.add(exp1.result);
                int i = exp1.position;
                theNextToken = getToken(i);
                if (theNextToken instanceof CommaToken) {
                    i = i + 1;
                }
                while (!(theNextToken instanceof RightParenToken)) {
                    ParseResult<Exp> exp2 = parseExp(i);
                    exps.add(exp2.result);
                    Token testToken = getToken(exp2.position);
                    if (testToken instanceof CommaToken) {
                        i = exp2.position + 1;
                    } else {
                        i = exp2.position;
                    }
                    theNextToken = getToken(i);
                }
                assertTokenHereIs(i, new RightParenToken());
                assertTokenHereIs(i + 1, new SemiColonToken());
                return new ParseResult<Stmt>(new PrintlnStmt(exps), i + 2);
            }
        } else if (token instanceof SuperToken) {
            final ParseResult<Stmt> superStmt = parseSuperStmt(position);
            return superStmt;
        } else if (token instanceof ThisToken) {
            final ParseResult<Stmt> thisStmt = parseThisStmt(position);
            return thisStmt;
        } else {
            final ParseResult<Exp> theExp = parseExp(position);
            assertTokenHereIs(theExp.position, new SemiColonToken());
            final ParseResult<Stmt> theExpStmt = new ParseResult<Stmt>(new ExpStmt(theExp.result), theExp.position + 1);
            return theExpStmt;
        }
    }

    public ParseResult<MethodName> parseMethod(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<MethodName>(new MethodName(name), position + 1);
        } else {
            throw new ParserException("Expected a VariableToken but received: " + token.toString());
        }
    }

    public ParseResult<MethodDef> parseMethodDef(final int position) throws ParserException {
        final Token token = getToken(position);
        if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StrToken)
                || (token instanceof VariableToken)) {
            final ParseResult<Type> type = parseType(position);
            final Token token2 = getToken(position + 1);
            final String nameToken2 = ((VariableToken) token2).name;
            assertTokenHereIs(position + 1, new VariableToken(nameToken2));

            final ParseResult<MethodName> methodname = parseMethod(position + 1);
            assertTokenHereIs(position + 2, new LeftParenToken());
            final List<Param> params = new ArrayList<Param>();
            Token nextToken = getToken(position + 3);
            if (nextToken instanceof RightParenToken) {
                final ParseResult<Stmt> stmt = parseStmt(position + 4);
                final ParseResult<MethodDef> result = new ParseResult<MethodDef>(
                        new MethodDef(type.result, methodname.result, params, stmt.result), stmt.position);
                return result;
            } else {
                ParseResult<Param> param = parseParam(position + 3);
                params.add(param.result);
                int i = param.position;
                nextToken = getToken(i);
                if (nextToken instanceof CommaToken) {
                    i = i + 1;
                }
                while (!(nextToken instanceof RightParenToken)) {
                    ParseResult<Param> nextParam = parseParam(i);
                    params.add(nextParam.result);
                    Token tryToken = getToken(nextParam.position);
                    if (tryToken instanceof CommaToken) {
                        i = nextParam.position + 1;
                    } else {
                        i = nextParam.position;
                    }
                    nextToken = getToken(i);
                }
                assertTokenHereIs(i, new RightParenToken());
                final ParseResult<Stmt> stmt1 = parseStmt(i + 1);
                final ParseResult<MethodDef> result1 = new ParseResult<MethodDef>(
                        new MethodDef(type.result, methodname.result, params, stmt1.result), stmt1.position);
                return result1;
            }
        } else {
            throw new ParserException("Expected a TypeToken but received: " + token.toString());
        }
    }

    public ParseResult<ClassName> parseClassName(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<ClassName>(new ClassName(name), position + 1);
        } else {
            throw new ParserException("Expected a VariableToken but received: " + token.toString());
        }
    }

    public ParseResult<ClassDef> parseClassdef(final int position) throws ParserException {
		final List<Param> params = new ArrayList<Param>();
		final Token token = getToken(position);
		if (token instanceof ClassToken) {
			final Token token1 = getToken(position + 1);
			final String name = ((VariableToken)token1).name;
			assertTokenHereIs(position + 1, new VariableToken(name));
			final ParseResult<ClassName> classname = parseClassName(position + 1);
			final Token token2 = getToken(classname.position);
			if (token2 instanceof ExtendsToken) {		
				final Token token3 = getToken(classname.position + 1);
				final String name1 = ((VariableToken)token3).name;
				assertTokenHereIs(classname.position + 1, new VariableToken(name1));
				final ParseResult<ClassName> extendsClassname = parseClassName(classname.position + 1);
				assertTokenHereIs(extendsClassname.position, new LeftCurlyToken());
				final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
				int keepTrack = extendsClassname.position + 1;
				Token nextToken = getToken(keepTrack);
				Token nextNextToken = getToken(keepTrack + 1);
 				while (((nextToken instanceof IntToken) || (nextToken instanceof BooleanToken) || (nextToken instanceof StrToken) ||(nextToken instanceof VariableToken))
						&& (nextNextToken instanceof VariableToken)) {		
					final ParseResult<VariableDeclaration> vardec = parseVarDec(keepTrack);
					vardecs.add(vardec.result);
					keepTrack = vardec.position;
					nextToken = getToken(keepTrack);
					nextNextToken = getToken(keepTrack + 1);
				}
				if ((nextToken instanceof VariableToken) && (nextNextToken instanceof LeftParenToken)) {	
					assertTokenHereIs(keepTrack, new VariableToken(name));	
					keepTrack = keepTrack + 2;
					Token token4 = getToken(keepTrack);
					while (!(token4 instanceof RightParenToken)) {	
						final ParseResult<Param> param = parseParam(keepTrack);
						params.add(param.result);
						Token ughToken0 = getToken(param.position);
						if (ughToken0 instanceof CommaToken) {
							keepTrack = param.position + 1;
						} else {
							keepTrack = param.position;
						}
						token4 = getToken(keepTrack);
					}			
				} else {
					throw new ParserException("Expected start of a constructor but received: " + nextToken.toString() + "," + nextNextToken.toString());
				}
				assertTokenHereIs(keepTrack, new RightParenToken());
				final ParseResult<Stmt> stmt = parseStmt(keepTrack + 1);	
				keepTrack = stmt.position;
				final List<MethodDef> methoddefs = new ArrayList<MethodDef>();
				Token token5 = getToken(keepTrack);
				while (!(token5 instanceof RightCurlyToken)) {
					final ParseResult<MethodDef> methoddef = parseMethodDef(keepTrack);
					methoddefs.add(methoddef.result);
					keepTrack = methoddef.position;
					token5 = getToken(keepTrack);
				}
				assertTokenHereIs(keepTrack, new RightCurlyToken());
				return new ParseResult<ClassDef>(new ClassDef(classname.result, extendsClassname.result, vardecs, params, stmt.result, methoddefs), keepTrack + 1);
			} else if (token2 instanceof LeftCurlyToken) {	
				assertTokenHereIs(classname.position, new LeftCurlyToken());
				final List<VariableDeclaration> vardecs1 = new ArrayList<VariableDeclaration>();
				int keepTrack1 = classname.position + 1;
				Token nextToken1 = getToken(keepTrack1);
				Token nextNextToken1 = getToken(keepTrack1 + 1);
 				while (((nextToken1 instanceof IntToken) || (nextToken1 instanceof BooleanToken) || (nextToken1 instanceof StrToken) ||(nextToken1 instanceof VariableToken))
						&& (nextNextToken1 instanceof VariableToken)) {	
					final ParseResult<VariableDeclaration> vardec1 = parseVarDec(keepTrack1);
					vardecs1.add(vardec1.result);
					keepTrack1 = vardec1.position;
					nextToken1 = getToken(keepTrack1);
					nextNextToken1 = getToken(keepTrack1 + 1);
				}
				if ((nextToken1 instanceof VariableToken) && (nextNextToken1 instanceof LeftParenToken)) {	
					assertTokenHereIs(keepTrack1, new VariableToken(name));	
					keepTrack1 = keepTrack1 + 2;
					Token token4v2 = getToken(keepTrack1);
					while (!(token4v2 instanceof RightParenToken)) {	
						final ParseResult<Param> param1 = parseParam(keepTrack1);
						params.add(param1.result);
						Token ughToken = getToken(param1.position);
						if (ughToken instanceof CommaToken) {
							keepTrack1 = param1.position + 1;
						} else {
							keepTrack1 = param1.position;
						}
						token4v2 = getToken(keepTrack1);
					}			
				} else {
					throw new ParserException("Expected start of a constructor but received: " + nextToken1.toString() + "," + nextNextToken1.toString());
				}
				assertTokenHereIs(keepTrack1, new RightParenToken());
				final ParseResult<Stmt> stmt1 = parseStmt(keepTrack1 + 1);	
				keepTrack1 = stmt1.position;
				final List<MethodDef> methoddefs1 = new ArrayList<MethodDef>();
				Token token5v2 = getToken(keepTrack1);
				while (!(token5v2 instanceof RightCurlyToken)) {
					final ParseResult<MethodDef> methoddef1 = parseMethodDef(keepTrack1);
					methoddefs1.add(methoddef1.result);
					keepTrack1 = methoddef1.position;
					token5v2 = getToken(keepTrack1);
				}
				assertTokenHereIs(keepTrack1, new RightCurlyToken());
				return new ParseResult<ClassDef>(new ClassDef(classname.result, new ClassName(""), vardecs1, params, stmt1.result, methoddefs1), keepTrack1 + 1);	
			} else {
				throw new ParserException("Expecting either extends or a left curly token but received: " + token2.toString());
			}
		} else {
			throw new ParserException("expected a class token but received: " + token.toString());
		}
	}

    public ParseResult<Program> parseProgram(final int position) throws ParserException {	
		final List<ClassDef> classes = new ArrayList<ClassDef>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		boolean shouldRunClasses = true;
		boolean shouldRunStmts = true;
		int newPosition = position;
		while (shouldRunClasses) {
			try {
				final ParseResult<ClassDef> theClass = parseClassdef(newPosition);
				classes.add(theClass.result);
				newPosition = theClass.position;
			} catch (final ParserException e) {
				shouldRunClasses = false;
			}
		}
		while (shouldRunStmts) {
			try {
				final ParseResult<Stmt> stmt = parseStmt(newPosition);
				stmts.add(stmt.result);
				newPosition = stmt.position;
			} catch (final ParserException e) {
				shouldRunStmts = false;
			}
		}
		return new ParseResult<Program>(new Program(classes, stmts), newPosition);
	}

    public Program parseProgram() throws ParserException {
		final ParseResult<Program> program = parseProgram(0);
		if (program.position == tokens.size()) {
			return program.result;
		} else {	
			throw new ParserException("Remaining tokens at end");
		}
	 }
	

}

// public ParseResult<VarDec> parseVarDec(final int position) throws
// ParserException {
// final Token token = getToken(position);
// if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token
// instanceof StrToken)
// || (token instanceof VariableToken)) {
// final Token nextToken = getToken(position + 1);
// final String name = ((VariableToken) nextToken).name;
// assertTokenHereIs(position + 1, new VariableToken(name));
// final Token nextNextToken = getToken(position + 2);
// if (nextNextToken instanceof AssignmentToken) {
// final ParseResult<Type> type = parseType(position);
// final ParseResult<Exp> variable = parsePrimaryExp(position + 1);
// final ParseResult<Exp> exp = parseExp(position + 3);
// assertTokenHereIs(position + 4, new SemiColonToken());
// return new ParseResult<VarDec>(new VariableDeclaration(type.result,
// variable.result, exp.result),
// position + 5);
// } else {
// throw new ParserException("Expected assignment operator; Received: " +
// nextNextToken);
// }
// } else {
// throw new ParserException("Expected variable declaration; Received: " +
// token);
// }
// }

// public ParseResult<Stmt> parseThisStmt(final int position) throws
// ParserException {
// final Token token = getToken(position);
// if(token instanceof ThisToken) {
// assertTokenHereIs(position + 1, new PeriodToken());
// final Token nextToken = getToken(position + 2);
// final String name = ((VariableToken) nextToken).name;
// assertTokenHereIs(position + 2, new VariableToken(name));
// assertTokenHereIs(position + 3, new AssignmentToken());
// final Token nextNextToken = getToken(position + 4);
// final String label = ((VariableToken) nextNextToken).name;
// assertTokenHereIs(position + 4, new VariableToken(label));
// assertTokenHereIs(position + 5, new SemiColonToken());
// return new ParseResult<Stmt>(new ThisStmt(new VariableExp(new
// Variable(name)), new VariableExp(new Variable(label))),position+6);
// } else {
// throw new ParserException("Expected this; Received: " + token);
// }
// }

// public ParseResult<Stmt> parseBreakStmt(final int position) throws
// ParserException {
// final Token token = getToken(position);
// if(token instanceof BreakToken){
// final Token nextToken = getToken(position + 1);
// final String name = ((BreakToken)token).toString();
// assertTokenHereIs(position + 1, new SemiColonToken());
// final String label = ((SemiColonToken)nextToken).toString();
// return new ParseResult<Stmt>(new BreakStmt(name, label), position + 2);
// } else {
// throw new ParserException("Expected break statement; Received: " + token);
// }
// }

// public ParseResult<Stmt> parseStmt(final int position) throws ParserException
// {
// final Token token = getToken(position);
// if ((token instanceof VariableToken) && (getToken(position + 1) instanceof
// AssignmentToken)) {
// final ParseResult<Exp> variable = parsePrimaryExp(position);
// final ParseResult<Exp> exp = parseExp(position + 2);
// assertTokenHereIs(exp.position, new SemiColonToken());
// return new ParseResult<Stmt>(new VarAssignStmt(variable.result, exp.result),
// exp.position + 1);
// } else if( (token instanceof IntToken) || (token instanceof BooleanToken) ||
// (token
// instanceof StringToken) || (token instanceof VariableToken)) {
// final Token nextToken = getToken(position + 1);
// if (nextToken instanceof VariableToken) {
// final String name = ((VariableToken)nextToken).name;
// assertTokenHereIs(position + 1, new VariableToken(name));
// final ParseResult<VarDec> varDec = parseVarDec(position);
// return new ParseResult<Stmt>(new VarDecStmt(varDec), varDec.position);
// } else {
// throw new ParserException("");
// }
// } else if (token instanceof WhileToken) {
// assertTokenHereIs(position + 1, new LeftParenToken());
// final ParseResult<Exp> guard = parseExp(position + 2);
// assertTokenHereIs(guard.position, new RightParenToken());
// final ParseResult<Stmt> stmt = parseStmt(guard.position + 1);
// return new ParseResult<Stmt>(new WhileStmt(guard.result, stmt.result),
// stmt.position);
// } else if (token instanceof BreakToken ) {
// final ParseResult<Stmt> result;
// result = parseBreakStmt(position);
// return result;
// } else if(token instanceof IfToken) {
// assertTokenHereIs(position + 1, new LeftParenToken());
// final ParseResult<Exp> ifGuard = parseExp(position + 2);
// assertTokenHereIs(ifGuard.position, new RightParenToken());
// final ParseResult<Stmt> trueBranch = parseStmt(ifGuard.position + 1);
// assertTokenHereIs(trueBranch.position, new ElseToken());
// final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
// return new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result,
// falseBranch.result), falseBranch.position);
// } else if (token instanceof ReturnToken) {
// final ParseResult<Exp> exp = parseExp(position + 1);
// assertTokenHereIs(exp.position, new SemiColonToken());
// return new ParseResult<Stmt>(new ReturnStmt(exp.result), exp.position + 1);
// } else if (token instanceof LeftCurlyToken) {
// List<Stmt> stmts = new ArrayList<>();
// Token nextToken = getToken(position + 1);
// if (nextToken instanceof RightCurlyToken) {
// return new ParseResult<Stmt>(new BlockStmt(stmts), position + 2);
// } else {
// ParseResult<Stmt> stmt = parseStmt(position + 1);
// stmts.add(stmt.result);
// int i = stmt.position;
// nextToken = getToken(i);
// while (!(nextToken instanceof RightCurlyToken)) {
// ParseResult<Stmt> nextStmt = parseStmt(i);
// stmts.add(nextStmt.result);
// i = i + 1;
// nextToken = getToken(i);
// }
// assertTokenHereIs(i, new RightCurlyToken());
// return new ParseResult<Stmt>(new BlockStmt(stmts), i + 1);
// }
// }

// else {
// final ParseResult<Exp> compExp = parseComparisonExp(position);
// final ParseResult<Stmt> compStmt = new ParseResult<Stmt>(new
// ExpStmt(compExp.result), compExp.position);
// return compStmt;
// }
// }
// }

/*
 * 
 * public ParseResult<Exp> parsePrimaryExp(final int position) throws
 * ParserException {
 * final Token token = getToken(position);
 * if (token instanceof VariableToken) {
 * final String name = ((VariableToken)token).name;
 * return new ParseResult<Exp>(new VariableExp(new Variable(name)),
 * position + 1);
 * }
 * // } else if(token instanceof StringTypeToken) ---- StringTypeToken or
 * StringToken{
 * 
 * // should we also handle thisexp and return exp here?
 * 
 * // }
 * 
 * else if (token instanceof IntegerToken) {
 * final int value = ((IntegerToken)token).value;
 * return new ParseResult<Exp>(new IntegerLiteralExp(value), position + 1);
 * } else if (token instanceof LeftParenToken) {
 * final ParseResult<Exp> inParens = parseExp(position + 1);
 * assertTokenHereIs(inParens.position, new RightParenToken());
 * return new ParseResult<Exp>(inParens.result,
 * inParens.position + 1);
 * } else {
 * throw new ParserException("Expected primary expression; received: " + token);
 * }
 * }
 * 
 * 
 * public ParseResult<Op> parseAdditiveOp(final int position) throws
 * ParserException {
 * final Token token = getToken(position);
 * if (token instanceof AdditionToken) {
 * return new ParseResult<Op>(new PlusOp(), position + 1);
 * } else if (token instanceof SubtractionToken) {
 * return new ParseResult<Op>(new SubtractionOp(), position + 1);
 * } else if (token instanceof MultiplicationToken) {
 * return new ParseResult<Op>(new MultiplicationOp(), position + 1);
 * } else if (token instanceof DivisionToken) {
 * return new ParseResult<Op>(new DivisionOp(), position + 1);
 * } else {
 * throw new ParserException("Expected additive operator; received: " + token);
 * }
 * }
 * 
 * public ParseResult<Exp> parseAdditiveExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parsePrimaryExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
 * final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * additiveOp.result,
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * }
 * 
 * return current;
 * }
 * 
 * public ParseResult<Exp> parseDotExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseAdditiveExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new PeriodToken());
 * final ParseResult<Exp> anotherPrimary = parsePrimaryExp(current.position +
 * 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new PeriodOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * 
 * public ParseResult<Exp> parseLessThanExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseDotExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new LessThanToken());
 * final ParseResult<Exp> anotherPrimary = parseDotExp(current.position + 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new LessThanOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * 
 * public ParseResult<Exp> parseGreaterThanExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseLessThanExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new GreaterThanToken());
 * final ParseResult<Exp> anotherPrimary = parseLessThanExp(current.position +
 * 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new GreaterThanOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * public ParseResult<Exp> parseDoubleEqualsExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseGreaterThanExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new EqualsToken());
 * final ParseResult<Exp> anotherPrimary = parseGreaterThanExp(current.position
 * + 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new DoubleEqualsOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * public ParseResult<Exp> parseNotEqualsExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseDoubleEqualsExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new NotEqualsToken());
 * final ParseResult<Exp> anotherPrimary = parseDoubleEqualsExp(current.position
 * + 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new NotEqualsOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * 
 * public ParseResult<Exp> parseEqualsExp(final int position) throws
 * ParserException {
 * ParseResult<Exp> current = parseNotEqualsExp(position);
 * boolean shouldRun = true;
 * 
 * while (shouldRun) {
 * try {
 * assertTokenHereIs(current.position, new AssignmentToken());
 * final ParseResult<Exp> anotherPrimary = parseNotEqualsExp(current.position +
 * 1);
 * current = new ParseResult<Exp>(new OpExp(current.result,
 * new EqualsOp(),
 * anotherPrimary.result),
 * anotherPrimary.position);
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * 
 * }
 * return current;
 * }
 * 
 * 
 * 
 * 
 * 
 * 
 * // public ParseResult<Exp> parseLessThanExp(final int position) throws
 * ParserException {
 * // ParseResult<Exp> current = parseAdditiveExp(position);
 * // boolean shouldRun = true;
 * 
 * // while (shouldRun) {
 * // try {
 * // assertTokenHereIs(current.position, new LessThanToken());
 * // final ParseResult<Exp> other = parseAdditiveExp(current.position + 1);
 * // current = new ParseResult<Exp>(new OpExp(current.result,
 * // new LessThanOp(),
 * // other.result),
 * // other.position);
 * // } catch (final ParserException e) {
 * // shouldRun = false;
 * // }
 * // }
 * 
 * // return current;
 * // }
 * 
 * // public ParseResult<Exp> parseEqualsExp(final int position) throws
 * ParserException {
 * // ParseResult<Exp> current = parseLessThanExp(position);
 * // boolean shouldRun = true;
 * 
 * // while (shouldRun) {
 * // try {
 * // assertTokenHereIs(current.position, new EqualsToken());
 * // final ParseResult<Exp> other = parseLessThanExp(current.position + 1);
 * // current = new ParseResult<Exp>(new OpExp(current.result,
 * // new EqualsOp(),
 * // other.result),
 * // other.position);
 * // } catch (final ParserException e) {
 * // shouldRun = false;
 * // }
 * // }
 * 
 * // return current;
 * // }
 * 
 * // public ParseResult<Exp> parseGreaterThanExp(final int position) throws
 * ParserException {
 * // ParseResult<Exp> current = parseEqualsExp(position);
 * // boolean shouldRun = true;
 * 
 * // while (shouldRun) {
 * // try {
 * // assertTokenHereIs(current.position, new GreaterThanToken());
 * // final ParseResult<Exp> other = parseEqualsExp(current.position + 1);
 * // current = new ParseResult<Exp>(new OpExp(current.result,
 * // new GreaterThanOp(),
 * // other.result),
 * // other.position);
 * // } catch (final ParserException e) {
 * // shouldRun = false;
 * // }
 * // }
 * 
 * // return current;
 * // }
 * 
 * // public ParseResult<Exp> parseDoubleEqualsExp(final int position) throws
 * ParserException {
 * // ParseResult<Exp> current = parseGreaterThanExp(position);
 * // boolean shouldRun = true;
 * 
 * // while (shouldRun) {
 * // try {
 * // assertTokenHereIs(current.position, new EqualsToken());
 * // final ParseResult<Exp> other = parseGreaterThanExp(current.position + 1);
 * // current = new ParseResult<Exp>(new OpExp(current.result,
 * // new DoubleEqualsOp(),
 * // other.result),
 * // other.position);
 * // } catch (final ParserException e) {
 * // shouldRun = false;
 * // }
 * // }
 * 
 * // return current;
 * // }
 * // public ParseResult<Exp> parseDivideExp(final int position) throws
 * ParserException {
 * // ParseResult<Exp> current = parseDoubleEqualsExp(position);
 * // boolean shouldRun = true;
 * 
 * // while (shouldRun) {
 * // try {
 * // assertTokenHereIs(current.position, new DivisionToken());
 * // final ParseResult<Exp> other = parseDoubleEqualsExp(current.position + 1);
 * // current = new ParseResult<Exp>(new OpExp(current.result,
 * // new DivisionOp(),
 * // other.result),
 * // other.position);
 * // } catch (final ParserException e) {
 * // shouldRun = false;
 * // }
 * // }
 * 
 * // return current;
 * // }
 * 
 * 
 * public ParseResult<Exp> parseExp(final int position) throws ParserException {
 * return parseEqualsExp(position);
 * }
 * public ParseResult<Stmt> parseStmt(final int position) throws ParserException
 * {
 * final Token token = getToken(position);
 * if (token instanceof IfToken) {
 * assertTokenHereIs(position + 1, new LeftParenToken());
 * final ParseResult<Exp> guard = parseExp(position + 2);
 * assertTokenHereIs(guard.position, new RightParenToken());
 * final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
 * assertTokenHereIs(trueBranch.position, new ElseToken());
 * final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
 * return new ParseResult<Stmt>(new IfStmt(guard.result,
 * trueBranch.result,
 * falseBranch.result),
 * falseBranch.position);
 * } else if (token instanceof LeftCurlyToken) {
 * final List<Stmt> stmts = new ArrayList<Stmt>();
 * int curPosition = position + 1;
 * boolean shouldRun = true;
 * while (shouldRun) {
 * try {
 * final ParseResult<Stmt> stmt = parseStmt(curPosition);
 * stmts.add(stmt.result);
 * curPosition = stmt.position;
 * } catch (final ParserException e) {
 * shouldRun = false;
 * }
 * }
 * return new ParseResult<Stmt>(new BlockStmt(stmts),
 * curPosition);
 * } else if (token instanceof PrintlnToken) {
 * assertTokenHereIs(position + 1, new LeftParenToken());
 * final ParseResult<Exp> exp = parseExp(position + 2);
 * assertTokenHereIs(exp.position, new RightParenToken());
 * assertTokenHereIs(exp.position + 1, new SemiColonToken());
 * return new ParseResult<Stmt>(new PrintlnStmt(exp.result),
 * exp.position + 2);
 * } else {
 * throw new ParserException("expected statement; received: " + token);
 * }
 * }
 * 
 * public ParseResult<Program> parseProgram(final int position) throws
 * ParserException {
 * final ParseResult<Stmt> stmt = parseStmt(position);
 * return new ParseResult<Program>(new Program(stmt.result),
 * stmt.position);
 * }
 * 
 * public Program parseProgram() throws ParserException {
 * final ParseResult<Program> program = parseProgram(0);
 * if (program.position == tokens.size()) {
 * return program.result;
 * } else {
 * throw new ParserException("Remaining tokens at end");
 * }
 * }
 * 
 * 
 */
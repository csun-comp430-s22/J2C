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
        } else if (token instanceof VoidToken) {
            return new ParseResult<Type>(new VoidType(), position + 1);
        } else if (token instanceof BooleanToken) {
            return new ParseResult<Type>(new BoolType(), position + 1);
        } else if (token instanceof StrToken) {
            return new ParseResult<Type>(new StringType(), position + 1);
        } else if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Type>(new ClassType(new ClassName(name)),
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

    public ParseResult<Exp> parseNewClassExp(final int position) throws ParserException {
        Token token = getToken(position + 1);
        final String name = ((VariableToken) token).name;
        assertTokenHereIs(position + 1, new VariableToken(name));
        final ParseResult<Exp> exp = parsePrimaryExp(position + 1);
        assertTokenHereIs(exp.position, new LeftParenToken());
        List<Exp> exps = new ArrayList<>();
        int i = exp.position + 1;
        token = getToken(i);
        ParseResult<Exp> next;
        if (token instanceof LeftParenToken) {
            next = new ParseResult<Exp>(new NewClassNameExp(exp.result, exps), i + 1);
        } else {
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    final ParseResult<Exp> nextExp = parsePrimaryExp(i);
                    exps.add(nextExp.result);
                    i = nextExp.position;
                } catch (final ParserException e) {
                    shouldRun = false;
                }

            }
            assertTokenHereIs(i, new RightParenToken());
            next = new ParseResult<Exp>(new NewClassNameExp(exp.result, exps), i + 1);
        }
        return next;
    }

    public ParseResult<Exp> parseVarMethodNameCallExp(final int position) throws ParserException {
        ParseResult<Exp> current = parsePrimaryExp(position);
        final Token token = getToken(current.position);
        if (token instanceof PeriodToken) {
            final ParseResult<Exp> next = parsePrimaryExp(current.position + 1);
            assertTokenHereIs(next.position, new LeftParenToken());
            List<Exp> exps = new ArrayList<>();
            int i = next.position + 1;
            Token nextToken = getToken(i);
            ParseResult<Exp> nextExp;
            if (nextToken instanceof LeftParenToken) {
                nextExp = new ParseResult<Exp>(new VarMethodNameCallExp(current.result, next.result, exps), i + 1);
            } else {
                boolean shouldRun = true;
                while (shouldRun) {
                    try {
                        final ParseResult<Exp> nextExp2 = parsePrimaryExp(i);
                        exps.add(nextExp2.result);
                        i = nextExp2.position;
                    } catch (final ParserException e) {
                        shouldRun = false;
                    }

                }
                assertTokenHereIs(i, new RightParenToken());
                nextExp = new ParseResult<Exp>(new VarMethodNameCallExp(current.result, next.result, exps), i + 1);
            }
            return nextExp;
        } else {
            return current;
        }

    }

    public ParseResult<Exp> parseExp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            try { 
                final Token nextToken = getToken(position + 1);
            if (nextToken instanceof PeriodToken) {
                return parseVarMethodNameCallExp(position);
            } else {
                return parseComparisonExp(position);
            }
            } catch (final ParserException e) {
                return parseComparisonExp(position);
            }
        } else if (token instanceof NewToken) {
            return parseNewClassExp(position);
        } else {
            return parseComparisonExp(position);
        }
    }

    public ParseResult<VarDec> parseVarDec(final int position) throws ParserException {
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
                assertTokenHereIs(position + 4, new SemiColonToken());
                return new ParseResult<VarDec>(new VariableDeclaration(type.result, variable.result, exp.result),
                        position + 5);
            } else {
                throw new ParserException("Expected assignment operator; Received: " + nextNextToken);
            }
        } else {
            throw new ParserException("Expected variable declaration; Received: " + token);
        }
    }

    public ParseResult<Stmt> parseThisStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        if(token instanceof ThisToken) {
            assertTokenHereIs(position + 1, new PeriodToken());
            final Token nextToken = getToken(position + 2);
            final String name = ((VariableToken) nextToken).name;
            assertTokenHereIs(position + 2, new VariableToken(name));
            assertTokenHereIs(position + 3, new AssignmentToken());
            final Token nextNextToken = getToken(position + 4);
            final String label = ((VariableToken) nextNextToken).name;
            assertTokenHereIs(position + 4, new VariableToken(label));
            assertTokenHereIs(position + 5, new SemiColonToken());
            return new ParseResult<Stmt>(new ThisStmt(new VariableExp(new Variable(name)), new VariableExp(new Variable(label))),position+6);
        } else {
            throw new ParserException("Expected this; Received: " + token);
        }
    }

    public ParseResult<Stmt> parseBreakStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        if(token instanceof BreakToken){
            final Token nextToken = getToken(position + 1);
            final String name = ((BreakToken)token).toString();
            assertTokenHereIs(position + 1, new SemiColonToken());
            final String label = ((SemiColonToken)nextToken).toString();
            return new ParseResult<Stmt>(new BreakStmt(name, label), position + 2);
        } else {
            throw new ParserException("Expected break statement; Received: " + token);
        }
    }

    public ParseResult<Stmt> parseStmt(final int position) throws ParserException {
		final Token token = getToken(position);
		if ((token instanceof VariableToken) && (getToken(position + 1) instanceof AssignmentToken)) {
			final ParseResult<Exp> variable = parsePrimaryExp(position);
			final ParseResult<Exp> exp = parseExp(position + 2);
			assertTokenHereIs(exp.position, new SemiColonToken());
			return new ParseResult<Stmt>(new VarAssignStmt(variable.result, exp.result), exp.position + 1);
		} else if( (token instanceof IntToken) || (token instanceof BooleanToken) || (token
				 instanceof StringToken)  || (token instanceof VariableToken)) {
			final Token nextToken = getToken(position + 1);
			if (nextToken instanceof VariableToken) {
				final String name = ((VariableToken)nextToken).name;
				assertTokenHereIs(position + 1, new VariableToken(name));
				final ParseResult<VarDec> varDec = parseVarDec(position);
				return new ParseResult<Stmt>(new VarDecStmt(varDec), varDec.position);
			} else {
				throw new ParserException("");
			}
		} else if (token instanceof WhileToken) {
			assertTokenHereIs(position + 1, new LeftParenToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new RightParenToken());
			final ParseResult<Stmt> stmt = parseStmt(guard.position + 1);
			return new ParseResult<Stmt>(new WhileStmt(guard.result, stmt.result), stmt.position);
		} else if (token instanceof BreakToken ) {
			final ParseResult<Stmt> result;
			result = parseBreakStmt(position);
			return result;
		} else if(token instanceof IfToken) {
			assertTokenHereIs(position + 1, new LeftParenToken());
			final ParseResult<Exp> ifGuard = parseExp(position + 2);
			assertTokenHereIs(ifGuard.position, new RightParenToken());
			final ParseResult<Stmt> trueBranch = parseStmt(ifGuard.position + 1);
			assertTokenHereIs(trueBranch.position, new ElseToken());
			final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
			return new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), falseBranch.position);
		} else if (token instanceof ReturnToken) {
			final ParseResult<Exp> exp = parseExp(position + 1);
			assertTokenHereIs(exp.position, new SemiColonToken());
			return new ParseResult<Stmt>(new ReturnStmt(exp.result), exp.position + 1);
		} else if (token instanceof LeftCurlyToken) {
			List<Stmt> stmts = new ArrayList<>();
			Token nextToken = getToken(position + 1);
			if (nextToken instanceof RightCurlyToken) {
				return new ParseResult<Stmt>(new BlockStmt(stmts), position + 2);
			} else {
				ParseResult<Stmt> stmt = parseStmt(position + 1);
				stmts.add(stmt.result);
				int i = stmt.position;
				nextToken = getToken(i);
				while (!(nextToken instanceof RightCurlyToken)) {
					ParseResult<Stmt> nextStmt = parseStmt(i);
					stmts.add(nextStmt.result);
					i = i + 1;
					nextToken = getToken(i);
				}
				assertTokenHereIs(i, new RightCurlyToken());
				return new ParseResult<Stmt>(new BlockStmt(stmts), i + 1);
			}
		} 
        
        // else if (token instanceof PrintlnToken) {
		//   assertTokenHereIs(position + 1, new LeftParenToken());
		//   final ParseResult<Exp> exp = parseExp(position + 2);
		//   assertTokenHereIs(exp.position, new RightParenToken());
		//   assertTokenHereIs(exp.position + 1, new SemiColonToken());
		//   return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
		//   } else if(token instanceof ThisToken) {
        //     final ParseResult<Stmt> thisStmt = parseThisStmt(position);
		// 	 return thisStmt;
        // }


        else {
            final ParseResult<Exp> compExp = parseComparisonExp(position);
			final ParseResult<Stmt> compStmt = new ParseResult<Stmt>(new ExpStmt(compExp.result), compExp.position);
			return compStmt;
        }
    }
}

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
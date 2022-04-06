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
    //     final Token token = getToken(position);
    //     if (token instanceof AdditionToken) {
    //        return new ParseResult<Op>(new PlusOp(), position + 1);
    //     } else if (token instanceof SubtractionToken) {
    //         return new ParseResult<Op>(new SubtractionOp(), position + 1);
    //     } else if (token instanceof LessThanToken) {
    //         return new ParseResult<Op>(new LessThanOp(), position + 1);
    //     } else if (token instanceof AssignmentToken) {
    //         return new ParseResult<Op>(new EqualsOp(), position + 1);
    //     } else if(token instanceof GreaterThanToken) {
    //         return new ParseResult<Op>(new GreaterThanOp(), position + 1);
    //     } else if(token instanceof MultiplicationToken) {
    //         return new ParseResult<Op>(new MultiplicationOp(), position + 1);
    //     } else if(token instanceof DivisionToken) {
    //         return new ParseResult<Op>(new DivisionOp(), position + 1);
    //     } else {
    //         throw new ParserException("Expected operator; Received: " + token);
    //     }
    // }

    public void assertTokenHereIs(final int position, final Token expected) throws ParserException {
        final Token received = getToken(position);
        if (!expected.equals(received)) {
            throw new ParserException("expected: " + expected + "; received: " + received);
        }
    }



    public ParseResult<Exp> parsePrimaryExp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken)token).name;
            return new ParseResult<Exp>(new VariableExp(new Variable(name)),
                                        position + 1);
        } else if (token instanceof IntegerToken) {
            final int value = ((IntegerToken)token).value;
            return new ParseResult<Exp>(new IntegerExp(value), position + 1);
        } else if (token instanceof LeftParenToken) {
            final ParseResult<Exp> inParens = parseExp(position + 1);
            assertTokenHereIs(inParens.position, new RightParenToken());
            return new ParseResult<Exp>(inParens.result,
                                        inParens.position + 1);
        } else {
            throw new ParserException("Expected primary expression; received: " + token);
        }
    }


    public ParseResult<Op> parseAdditiveOp(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof AdditionToken) {
            return new ParseResult<Op>(new PlusOp(), position + 1);
        } else if (token instanceof SubtractionToken) {
            return new ParseResult<Op>(new SubtractionOp(), position + 1);
        } else if (token instanceof MultiplicationToken) {
            return new ParseResult<Op>(new MultiplicationOp(), position + 1);
        } else if (token instanceof DivisionToken) {
            return new ParseResult<Op>(new DivisionOp(), position + 1);
        } else {
            throw new ParserException("Expected additive operator; received: " + token);
        }
    } 

    public ParseResult<Exp> parseAdditiveExp(final int position) throws ParserException {
        ParseResult<Exp> current = parsePrimaryExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
                final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         additiveOp.result,
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
        }

        return current;
    } 

    public ParseResult<Exp> parseDotExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseAdditiveExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new PeriodToken());
                final ParseResult<Exp> anotherPrimary = parsePrimaryExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new PeriodOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }

    public ParseResult<Exp> parseLessThanExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseDotExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new LessThanToken());
                final ParseResult<Exp> anotherPrimary = parseDotExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new LessThanOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }

    public ParseResult<Exp> parseGreaterThanExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseLessThanExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new GreaterThanToken());
                final ParseResult<Exp> anotherPrimary = parseLessThanExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new GreaterThanOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }
    public ParseResult<Exp> parseDoubleEqualsExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseGreaterThanExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new EqualsToken());
                final ParseResult<Exp> anotherPrimary = parseGreaterThanExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new DoubleEqualsOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }
    public ParseResult<Exp> parseNotEqualsExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseDoubleEqualsExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new NotEqualsToken());
                final ParseResult<Exp> anotherPrimary = parseDoubleEqualsExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new NotEqualsOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }

    public ParseResult<Exp> parseEqualsExp(final int position) throws ParserException {
        ParseResult<Exp> current = parseNotEqualsExp(position);
        boolean shouldRun = true;
        
        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new AssignmentToken());
                final ParseResult<Exp> anotherPrimary = parseNotEqualsExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                                                         new EqualsOp(),
                                                         anotherPrimary.result),
                                               anotherPrimary.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
          
        }
        return current; 
    }

 
    



    // public ParseResult<Exp> parseLessThanExp(final int position) throws ParserException {
    //     ParseResult<Exp> current = parseAdditiveExp(position);
    //     boolean shouldRun = true;
        
    //     while (shouldRun) {
    //         try {
    //             assertTokenHereIs(current.position, new LessThanToken());
    //             final ParseResult<Exp> other = parseAdditiveExp(current.position + 1);
    //             current = new ParseResult<Exp>(new OpExp(current.result,
    //                                                      new LessThanOp(),
    //                                                      other.result),
    //                                            other.position);
    //         } catch (final ParserException e) {
    //             shouldRun = false;
    //         }
    //     }

    //     return current;
    // }

    // public ParseResult<Exp> parseEqualsExp(final int position) throws ParserException {
    //     ParseResult<Exp> current = parseLessThanExp(position);
    //     boolean shouldRun = true;

    //     while (shouldRun) {
    //         try {
    //             assertTokenHereIs(current.position, new EqualsToken());
    //             final ParseResult<Exp> other = parseLessThanExp(current.position + 1);
    //             current = new ParseResult<Exp>(new OpExp(current.result,
    //                                                      new EqualsOp(),
    //                                                      other.result),
    //                                            other.position);
    //         } catch (final ParserException e) {
    //             shouldRun = false;
    //         }
    //     }

    //     return current;
    // } 

    // public ParseResult<Exp> parseGreaterThanExp(final int position) throws ParserException {
    //     ParseResult<Exp> current = parseEqualsExp(position);
    //     boolean shouldRun = true;

    //     while (shouldRun) {
    //         try {
    //             assertTokenHereIs(current.position, new GreaterThanToken());
    //             final ParseResult<Exp> other = parseEqualsExp(current.position + 1);
    //             current = new ParseResult<Exp>(new OpExp(current.result,
    //                                                      new GreaterThanOp(),
    //                                                      other.result),
    //                                            other.position);
    //         } catch (final ParserException e) {
    //             shouldRun = false;
    //         }
    //     }

    //     return current;
    // }

    // public ParseResult<Exp> parseDoubleEqualsExp(final int position) throws ParserException {
    //     ParseResult<Exp> current = parseGreaterThanExp(position);
    //     boolean shouldRun = true;

    //     while (shouldRun) {
    //         try {
    //             assertTokenHereIs(current.position, new EqualsToken());
    //             final ParseResult<Exp> other = parseGreaterThanExp(current.position + 1);
    //             current = new ParseResult<Exp>(new OpExp(current.result,
    //                                                      new DoubleEqualsOp(),
    //                                                      other.result),
    //                                            other.position);
    //         } catch (final ParserException e) {
    //             shouldRun = false;
    //         }
    //     }

    //     return current;
    // }
    // public ParseResult<Exp> parseDivideExp(final int position) throws ParserException {
    //     ParseResult<Exp> current = parseDoubleEqualsExp(position);
    //     boolean shouldRun = true;

    //     while (shouldRun) {
    //         try {
    //             assertTokenHereIs(current.position, new DivisionToken());
    //             final ParseResult<Exp> other = parseDoubleEqualsExp(current.position + 1);
    //             current = new ParseResult<Exp>(new OpExp(current.result,
    //                                                      new DivisionOp(),
    //                                                      other.result),
    //                                            other.position);
    //         } catch (final ParserException e) {
    //             shouldRun = false;
    //         }
    //     }

    //     return current;
    // }
    

    public ParseResult<Exp> parseExp(final int position) throws ParserException {
        return parseEqualsExp(position);
    }
    public ParseResult<Stmt> parseStmt(final int position) throws ParserException {
        final Token token = getToken(position);
        if (token instanceof IfToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> guard = parseExp(position + 2);
            assertTokenHereIs(guard.position, new RightParenToken());
            final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
            return new ParseResult<Stmt>(new IfStmt(guard.result,
                                                   trueBranch.result,
                                                   falseBranch.result),
                                         falseBranch.position);
        } else if (token instanceof LeftCurlyToken) {
            final List<Stmt> stmts = new ArrayList<Stmt>();
            int curPosition = position + 1;
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    final ParseResult<Stmt> stmt = parseStmt(curPosition);
                    stmts.add(stmt.result);
                    curPosition = stmt.position;
                } catch (final ParserException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts),
                                         curPosition);
        } else if (token instanceof PrintlnToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            assertTokenHereIs(exp.position + 1, new SemiColonToken());
            return new ParseResult<Stmt>(new PrintlnStmt(exp.result),
                                         exp.position + 2);
        } else {
            throw new ParserException("expected statement; received: " + token);
        }
    }

    public ParseResult<Program> parseProgram(final int position) throws ParserException {
        final ParseResult<Stmt> stmt = parseStmt(position);
        return new ParseResult<Program>(new Program(stmt.result),
                                        stmt.position);
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
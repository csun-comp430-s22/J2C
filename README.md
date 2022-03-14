
# Project Title
Design a programming language


The goal for us is to pass this class and try to learn how to make a compiler and since we are all familiar with Java it makes sense to do it in Java. We are choosing C as our target language because some of our members have some experience with C and it's not too low level for us to understand and explain to other members of the group.


## Appendix 
Design this programming language with:
    • Concrete and abstract syntax 
    • Statically-checked types 
    • Expressions 
    • Subroutines 
    • Mechanisms for computation abstraction 
• Implement a compiler for the designed language, complete with: 
    • A parser 
    • A typechecker / static semantic analyzer 
    • A code generator
## Authors
    Ibtehaz Utsay
    Ziaur Chowdhury
    Noah Reidinger
    Ryan Nieto
    Leo Babakhanian
    Vardges Harutyunyan

## Documentation

Documentation Link (https://docs.google.com/document/d/1mQWRqwIdn_nY9M3pWh7nB8k_Ht-t0xxur-jjfsBvdB4/edit)

Language Name: J2C 

Compiler Implementation Language and Reasoning: We are all comfortable working with Java and it’s needless to experiment with a new language at this time. 

Planned Restrictions: We don’t know quite yet.

Target Language: C

Language Description: J2C (Java to C). The goal for us is to pass this class and try to learn how to make a compiler and since we are all familiar with Java it makes sense to do it in Java. We are choosing C as our target language because some of our members have some experience with C and it's not too low level for us to understand and explain to other members of the group.

Abstract Syntax:
var is a variable
classname is the name of a class
methodname is the name of a method
str is a string
i is an integer
 
type:: = Int | Boolean | Void | Built-in types
    classname; includes Object and String
op:: = + | - | * | / | < | > | == Arithmetic operations
exp:: = var | str | i | Variables, strings, and integers are expressions
this | Refers to my instance
exp op exp | Arithmetic operations
new classname(exp*) | Creates a new instance of a class
exp. methodname(exp*) | Calls a method
(type)exp | Casts an expression as a type/Runtime exception for improper casting
vardec:: = type var | Variable declaration
stmt:: = vardec = exp; | Variable declaration
var = exp; | Assignment
while (exp) stmt | while loops
break; | break
{ stmt* } | block
if (exp) stmt else stmt | if/else
return exp; | return an expression
return; | return Void
println(exp) | Prints something to the terminal
exp. methodname(exp*) | Calls a method
access ::= public | private | protected
methoddef ::= access type methodname(vardec*) stmt vardecs are comma-separated 
instancedec ::= access vardec; instance variable declaration classdef ::= class classname extends classname {
instancedec* 
constructor(vardec*) stmt vardecs are comma-separated 
methoddef*
} 
program ::= classdef* exp exp is the entry point?

Computation Abstraction Non-Trivial Feature: Class based inheritance + Methods

Non-Trivial Feature #2: Subtyping

Non-Trivial Feature #3: Type Casting

Work Planned for Custom Component: Access Modifiers

## Contributing

Contributions are always welcome!

See `contributing.md` for ways to get started.

Please adhere to this project's `code of conduct`.


## Demo

Insert gif or link to demo


## Deployment

To deploy this project run

```bash
  npm run deploy
```

### Adition Token
package lexer;

public class AdditionToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof AdditionToken;
    }
    public int hashCode() {
        return 8;
    }
    public String toString() {
        return "+";
    }
    
}

### Assignment Token
package lexer;

public class AssignmentToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof AssignmentToken;
        }
        public int hashCode() {
            return 38;
        }
        
        public String toString() {
            return "=";
        }
        
    
}

### Boolean Token
package lexer;

public class BooleanToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof BooleanToken;
    }
    public int hashCode() {
        return 72;
    }
    public String toString() {
        return "Bool";
    }
}

### Break Token
package lexer;

public class BreakToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof BreakToken;
        }
        public int hashCode() {
            return 33;
        }
        
        public String toString() {
            return "break";
        }
    
}

### ClassToken
package lexer;

public class ClassToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ClassToken;
    }
    public int hashCode() {
        return 10;
    }
    public String toString() {
        return "class";
    }
    
}


### Division Token
package lexer;

public class DivisionToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof DivisionToken;
        }
        public int hashCode() {
            return 31;
        }
        
        public String toString() {
            return "/";
        }
        
    
}

### Else Token
package lexer;

public class ElseToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ElseToken;
    }
    public int hashCode() {
        return 2;
    }
    public String toString() {
        return "else";
    }
}

### Equals Token
package lexer;

public class EqualsToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof EqualsToken;
    }
    public int hashCode() {
        return 40;
    }
    
    public String toString() {
        return "==";
    }
    
}

### Extends Token
package lexer;

public class ExtendsToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ExtendsToken;
    }
    public int hashCode() {
        return 11;
    }
    public String toString() {
        return "extends";
    }
    
}

### False Token

package lexer;

public class FalseToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof FalseToken;
    }
    public int hashCode() {
        return 16;
    }
    public String toString() {
        return "false";
    }
    
}

### GreaterThan Token

package lexer;

public class GreaterThanToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanToken;
    }
    public int hashCode() {
        return 12;
    }
    public String toString() {
        return ">";
    }
    
}

### If Token

package lexer;

public class IfToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof IfToken;
    }
    public int hashCode() {
        return 1;
    }
    public String toString() {
        return "if";
    }

}

### Integer Token

package lexer;

public class IntegerToken implements Token {

    public final int value;

    public IntegerToken(final int value) {
        this.value = value;
    }
    public boolean equals(final Object other) {
        if (other instanceof IntegerToken) {
            final IntegerToken asInt = (IntegerToken)other;
            return value == asInt.value;
        } else {
            return false;
        }
    }
    public int hashCode() {
        return value;
    }
    public String toString() {
        return "IntegerToken(" + value + ")";
    }
    
}

### LeftCurly Token
package lexer;

public class LeftCurlyToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof LeftCurlyToken;
    }
    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "{";
    }
    
}

### LeftParen Token

package lexer;

public class LeftParenToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftParenToken;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return "(";
    }
}

### LessThan Token

package lexer;

public class LessThanToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof LessThanToken;
    }
    public int hashCode() {
        return 13;
    }
    
    public String toString() {
        return "<";
    }
    
}

### Modulo Token

package lexer;

public class ModuloToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ModuloToken;
    }
    public int hashCode() {
        return 14;
    }
    public String toString() {
        return "%";
    }
    
    
}

### Multiplication Token

package lexer;

public class MultiplicationToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof MultiplicationToken;
    }
    public int hashCode() {
        return 15;
    }
    public String toString() {
        return "*";
    }
    
    
}

### New Token

package lexer;

public class NewToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof NewToken;
    }
    public int hashCode() {
        return 111;
    }
    
    public String toString() {
        return "new";
    }
    
}

### NotToken

package lexer;

public class NotToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof NotToken;
    }
    public int hashCode() {
        return 152;
    }
    public String toString() {
        return "!";
    }
    
    
}

### Println Token

package lexer;

public class PrintlnToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof PrintlnToken;
    }

    public int hashCode() {
        return 71;
    }

    public String toString() {
        return "println";
    }
}

### Return Token

package lexer;

public class ReturnToken implements Token {
    
        public boolean equals(final Object other) {
            return other instanceof ReturnToken;
        }
        public int hashCode() {
            return 35;
        }
        
        public String toString() {
            return "return";
        }
        
    
}

### RightCurly Token

package lexer;

public class RightCurlyToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightCurlyToken;
    }
    public int hashCode() {
        return 4;
    }
    public String toString() {
        return "}";
    }
}

### RightParen Token

package lexer;

public class RightParenToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightParenToken;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return ")";
    }
}

### SemiColon Token

package lexer;

public class SemiColonToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof SemiColonToken;
    }
    public int hashCode() {
        return 7;
    }
    public String toString() {
        return ";";
    }
    
}

### StringType Token

package lexer;

public class StringTypeToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof StringTypeToken;
    }

    public int hashCode() {
        return 73;
    }

    public String toString() {
        return "string";
    }

}

### Subtraction Token

package lexer;

public class SubtractionToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof SubtractionToken;
    }
    public int hashCode() {
        return 9;
    }
    public String toString() {
        return "-";
    }
    
}

### This Token

package lexer;

public class ThisToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ThisToken;
    }
    public int hashCode() {
        return 30;
    }
    public String toString() {
        return "this";
    } 
}

### Token

package lexer;

public interface Token {
}

### Tokenizer
package lexer;

import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    private final String input;
    private int offset;

    public Tokenizer(final String input) {
        this.input = input;
        offset = 0;
    }

    public void skipWhitespace() {
        while (offset < input.length() &&
                Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    public IntegerToken tryTokenizeInteger() {
        skipWhitespace();

        String number = "";

        while (offset < input.length() &&
                Character.isDigit(input.charAt(offset))) {
            number += input.charAt(offset);
            offset++;
        }

        if (number.length() > 0) {
            return new IntegerToken(Integer.parseInt(number));
        } else {
            return null;
        }
    }

    public Token tryTokenizeVariableOrKeyword() {
        skipWhitespace();

        String name = "";

        if (offset < input.length() &&
                Character.isLetter(input.charAt(offset))) {
            name += input.charAt(offset);
            offset++;

            while (offset < input.length() &&
                    Character.isLetterOrDigit(input.charAt(offset))) {
                name += input.charAt(offset);
                offset++;
            }

            if (name.equals("true")) {
                return new TrueToken();
            } else if (name.equals("false")) {
                return new FalseToken();
            } else if (name.equals("if")) {
                return new IfToken();
            } else if (name.equals("else")) {
                return new ElseToken();
            } else if (name.equals("class")) {
                return new ClassToken();
            } else if (name.equals("while")) {
                return new WhileToken();
            } else if (name.equals("this")) {
                return new ThisToken();
            } else if (name.equals("extends")) {
                return new ExtendsToken();
            } else if (name.equals("new")) {
                return new NewToken();
            } else if (name.equals("return")) {
                return new ReturnToken();
            } else if (name.equals("break")) {
                return new BreakToken();
            } else if (name.equals("void")) {
                return new VoidTypeToken();
            } else if (name.equals("bool")) {
                return new BooleanTypeToken();
            } else if (name.equals("println")) {
                return new PrintlnToken();
            } else if (name.equals("string")) {
                return new StringTypeToken();
            } else if (name.equals("int")) {
                return new IntTypeToken();
            } else {
                return new VariableToken(name);
            }
        } else {
            return null;
        }
    }

    public Token tryTokenizeSymbol() {
        skipWhitespace();
        Token retval = null;

        if (input.startsWith("(", offset)) {
            offset += 1;
            retval = new LeftParenToken();
        } else if (input.startsWith(")", offset)) {
            offset += 1;
            retval = new RightParenToken();
        } else if (input.startsWith("{", offset)) {
            offset += 1;
            retval = new LeftCurlyToken();
        } else if (input.startsWith("}", offset)) {
            offset += 1;
            retval = new RightCurlyToken();
        } else if (input.startsWith("+", offset)) {
            offset += 1;
            retval = new AdditionToken();
        } else if (input.startsWith("!", offset)) {
            offset += 1;
            retval = new NotToken();
        } else if (input.startsWith("%", offset)) {
            offset += 1;
            retval = new ModuloToken();
        } else if (input.startsWith(">", offset)) {
            offset += 1;
            retval = new GreaterThanToken();
        } else if (input.startsWith("<", offset)) {
            offset += 1;
            retval = new LessThanToken();
        } else if (input.startsWith("*", offset)) {
            offset += 1;
            retval = new MultiplicationToken();
        } else if (input.startsWith("-", offset)) {
            offset += 1;
            retval = new SubtractionToken();
        } else if (input.startsWith(";", offset)) {
            offset += 1;
            retval = new SemiColonToken();
        } else if (input.startsWith("/", offset)) {
            offset += 1;
            retval = new DivisionToken();
        } else if (input.startsWith("=", offset)) {
            offset += 1;
            retval = new AssignmentToken();
        }
        return retval;
    }

    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhitespace();
        if (offset < input.length() &&
                (retval = tryTokenizeVariableOrKeyword()) == null &&
                (retval = tryTokenizeInteger()) == null &&
                (retval = tryTokenizeSymbol()) == null) {
            throw new TokenizerException();
        }

        return retval;
    }

    public List<Token> tokenize() throws TokenizerException {
        final List<Token> tokens = new ArrayList<Token>();
        Token token = tokenizeSingle();

        while (token != null) {
            tokens.add(token);
            token = tokenizeSingle();
        }

        return tokens;
    }
}

### TokenizerException

package lexer;

public class TokenizerException extends Exception {
   
}

### TrueToken

package lexer;

public class TrueToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof TrueToken;
    }
    public int hashCode() {
        return 3;
    }
    public String toString() {
        return "true";
    }

}

### VariableToken

package lexer;

public class VariableToken implements Token {
    public final String name;

    public VariableToken(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return "Variable(" + name + ")";
    }

    public boolean equals(final Object other) {
        if (other instanceof VariableToken) {
            final VariableToken asVar = (VariableToken)other;
            return name.equals(asVar.name);
        } else {
            return false;
        }
    }
}

### VoidTypeToken

package lexer;

public class VoidTypeToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof VoidTypeToken;
    }

    public int hashCode() {
        return 70;
    }

    public String toString() {
        return "void";
    }

}

### WhileToken

package lexer;

public class WhileToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof WhileToken;
    }
    public int hashCode() {
        return 4;
    }
    public String toString() {
        return "while";
    }
}





## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`API_KEY`

`ANOTHER_API_KEY`


## License

[MIT](https://choosealicense.com/licenses/mit/)


## Roadmap

- Additional browser support

- Add more integrations


## Running Tests

To run tests, run the following command

```bash
  npm run test
```

package lexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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
                        new Token[] { new VariableToken("truetrue")});
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
    //     assertTokenizes("%",
    //                     new Token[] { new ModuloToken() });
    // }
    // @Test
    // public void testGreaterThan() {
    //     assertTokenizes(">",
    //                     new Token[] { new GreaterThanToken() });
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
    public void testAssignmentToken() throws TokenizerException {
        assertTokenizes("=",
                        new Token[] { new AssignmentToken() });
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




    
}
## Screenshots

![Logo](images/J2Clogo.png)


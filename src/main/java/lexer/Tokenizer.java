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
            } else {
                return new VariableToken(name);
            }
        } else {
            return null;
        }
    }
    
    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhitespace();
        if (offset < input.length()) {
            retval = tryTokenizeVariableOrKeyword();
            if (retval == null) {
                if (input.startsWith("(", offset)) {
                    offset += 1;
                    retval = new LeftParenToken();
                } else if (input.startsWith(")", offset)) {
                    offset += 1;
                    retval = new RightParenToken();
                }  else if (input.startsWith("{", offset)) {
                    offset += 1;
                    retval = new LeftCurlyToken();
                } else if (input.startsWith("}", offset)) {
                    offset += 1;
                    retval = new RightCurlyToken();
                } else {
                    throw new TokenizerException();
                }
            }
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
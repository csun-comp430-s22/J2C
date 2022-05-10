package typechecker;

public class TypeErrorException extends Exception {
    private static final long serialVersionUID = 1L;
    public TypeErrorException(String message) {
        super(message);
    }

}

package parser;
import java.util.List;

public class NewClassNameExp implements Exp {
    public final ClassNameExp className;
    public final List<Exp> args;

    public NewClassNameExp(final ClassNameExp className, final List<Exp> args) {
        this.className = className;
        this.args = args;
    }

    public boolean equals(final Object o) {
        if (o instanceof NewClassNameExp) {
            final NewClassNameExp other = (NewClassNameExp) o;
            return className.equals(other.className) && args.equals(other.args);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return className.hashCode() + args.hashCode();
    }

    public String toString() {
        return ("NewClassNameExp(" + className.toString() + ", " + args.toString() + ")");
    }


    
}

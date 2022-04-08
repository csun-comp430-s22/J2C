package parser;

import java.util.List;

public class NewClassNameExp implements Exp {

    public final Exp classname; 
    public final List<Exp> args;
    
    public NewClassNameExp(final Exp classname, final List<Exp> args) {
        this.classname = classname;
        this.args = args;
    }
    public boolean equals(final Object other) {
        return (other instanceof NewClassNameExp &&
                classname.equals(((NewClassNameExp)other).classname) &&
                args.equals(((NewClassNameExp)other).args));
    }
    public int hashCode() {
        return classname.hashCode() + args.hashCode();
    }
    public String toString() {
        return "NewClassNameExp(" + classname + ", " + args + ")";
    }

}

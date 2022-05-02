package parser;
import java.util.List;


public class MethodDef {
    public final Type returnType;
    public final MethodName mName;
    public final List<Param> params;
    public final Stmt body;

    public MethodDef(final Type returnType, final MethodName mName, final List<Param> params, final Stmt body) {
        this.returnType = returnType;
        this.mName = mName;
        this.params = params;
        this.body = body;
    }

    public int hashCode() {
        return returnType.hashCode() + mName.hashCode() + params.hashCode() + body.hashCode();
    }


    public boolean equals(final Object o) {
        if (o instanceof MethodDef) {
            final MethodDef other = (MethodDef) o;
            return returnType.equals(other.returnType) && mName.equals(other.mName) && params.equals(other.params) && body.equals(other.body);
        } else {
            return false;
        }
       
    }
    public String toString() {
        return ("MethodDef(" + returnType.toString() + ", " + mName.toString() + ", " + params.toString() + ", " + body.toString() + ")");
        
    }


    
}

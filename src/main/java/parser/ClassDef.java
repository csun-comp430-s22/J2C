package parser;

import java.util.List;


public class ClassDef {
    public final ClassName className;
    public final ClassName extendsClassName;
    public final List<VariableDeclaration> varDecs;
    public final List<MethodDef> methods;
    public final List<Param> params;
    public final Stmt body;

    public ClassDef(final ClassName className, final ClassName extendsClassName, final List<VariableDeclaration> varDecs, final List<Param> params, final Stmt body, final List<MethodDef> methods) {
        this.className = className;
        this.extendsClassName = extendsClassName;
        this.varDecs = varDecs;
        this.methods = methods;
        this.params = params;
        this.body = body;
    }

    public int hashCode() {
        return className.hashCode() + extendsClassName.hashCode() + varDecs.hashCode() + methods.hashCode() + params.hashCode() + body.hashCode();
    }

    public boolean equals(final Object o) {
        if (o instanceof ClassDef) {
            final ClassDef other = (ClassDef) o;
            return className.equals(other.className) && extendsClassName.equals(other.extendsClassName) && varDecs.equals(other.varDecs) && methods.equals(other.methods) && params.equals(other.params) && body.equals(other.body);
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassDef(" + className.toString() + ", " + extendsClassName.toString() + ", " + varDecs.toString() + ", " + methods.toString() + ", " + params.toString() + ", " + body.toString() + ")");
    }


    
}

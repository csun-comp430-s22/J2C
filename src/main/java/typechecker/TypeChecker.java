package typechecker;
import java.util.Map;

import parser.*;

public class TypeChecker {

    public static Type typeofMethodCall(final MethodCallExp, final Map<Variable, Type> typeEnvironment) throws TypeCheckerException {
        final Type leftType = typeofExp(left, typeEnvironment);
        final Type methodNameType = typeofExp(methodName, typeEnvironment);
        final List<Exp> args = args;
        if (leftType instanceof ClassType) {
            final ClassType classType = (ClassType) leftType;
            final Method method = classType.getMethod(methodNameType);
            if (method == null) {
                throw new TypeCheckerException("Method " + methodNameType + " not found in class " + classType);
            }
            if (args.size() != method.getNumParams()) {
                throw new TypeCheckerException("Method " + methodNameType + " in class " + classType + " takes " + method.getNumParams() + " arguments, but " + args.size() + " were given");
            }
            for (int i = 0; i < args.size(); i++) {
                final Exp arg = args.get(i);
                final Type argType = typeofExp(arg, typeEnvironment);
                if (!argType.equals(method.getParamType(i))) {
                    throw new TypeCheckerException("Argument " + i + " of method " + methodNameType + " in class " + classType + " has type " + argType + ", but should have type " + method.getParamType(i));
                }
            }
            return method.getReturnType();
        } else {
            throw new TypeCheckerException("Left side of method call must be a class, but is " + leftType);
        }
    }

    public static Type typeofOpExp(final OpExp exp, final Map<Variable, Type> typeEnvironment) throws TypeCheckerException {
        final Type leftType = typeof(exp.left, typeEnvironment);
        final Type rightType = typeof(exp.right, typeEnvironment);
        if(exp.op instanceof PlusOp) {
            if(leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeCheckerException("Type error: cannot add " + leftType + " and " + rightType);
            }
        } else if (exp.op instanceof SubtractionOp) {
            if(leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeCheckerException("Type error: cannot subtract " + leftType + " and " + rightType);
            }
        } else if (exp.op instanceof MultiplicationOp) {
            if(leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeCheckerException("Type error: cannot multiply " + leftType + " and " + rightType);
            }
        } else if (exp.op instanceof DivisionOp) {
            if(leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeCheckerException("Type error: cannot divide " + leftType + " and " + rightType);
            }
        } else if (exp.op instanceof LessThanOp) {
            if(leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeCheckerException("Type error: cannot compare " + leftType + " and " + rightType);
            }
        } else {
            throw new TypeCheckerException("Type error: unknown operator " + exp.op);
        }
    }

    public static Type typeof(final Exp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
        if(exp instanceof TrueExp){ 
            return new BoolType();
        } else if(exp instanceof FalseExp) {
            return new BoolType();
        } else if(exp instanceof IntegerLiteralExp) {
            return new IntType();
        } else if(exp instanceof OpExp) {
            return new typeofOpExp((OpExp)exp, typeEnvironment);
        } else if (exp instanceof VarMethodCallExp) {
            return new typeofMethodCall((VarMethodCallExp)exp, typeEnvironment);
        }
    }
    
}

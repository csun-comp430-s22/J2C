// package typechecker;
// import java.util.Map;
// import java.util.List;

// import parser.*;

// public class TypeChecker {

//     public static Type typeofMethodCall(final MethodCallExp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
        
        
//     }

//     public static Type typeofOpExp(final OpExp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
//         final Type leftType = typeof(exp.left, typeEnvironment);
//         final Type rightType = typeof(exp.right, typeEnvironment);
//         if(exp.op instanceof PlusOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new IntType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot add " + leftType + " and " + rightType);
//             }
//         } else if (exp.op instanceof SubtractionOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new IntType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot subtract " + leftType + " and " + rightType);
//             }
//         } else if (exp.op instanceof MultiplicationOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new IntType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot multiply " + leftType + " and " + rightType);
//             }
//         } else if (exp.op instanceof DivisionOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new IntType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot divide " + leftType + " and " + rightType);
//             }
//         } else if (exp.op instanceof LessThanOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new BoolType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot compare " + leftType + " and " + rightType);
//             }
//         } else if(exp.op instanceof GreaterThanOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new BoolType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot compare " + leftType + " and " + rightType);
//             }
//         }  else if(exp.op instanceof EqualsOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new BoolType();
//             } else if(leftType instanceof BoolType && rightType instanceof BoolType) {
//                 return new BoolType();
//             } else if(leftType instanceof StringType && rightType instanceof StringType) {
//                 return new BoolType();
//             } else if(leftType instanceof ClassType && rightType instanceof ClassType) {
//                 return new BoolType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot compare " + leftType + " and " + rightType);
//             }
//         } else if(exp.op instanceof NotEqualsOp) {
//             if(leftType instanceof IntType && rightType instanceof IntType) {
//                 return new BoolType();
//             } else if(leftType instanceof BoolType && rightType instanceof BoolType) {
//                 return new BoolType();
//             } else if(leftType instanceof StringType && rightType instanceof StringType) {
//                 return new BoolType();
//             } else if(leftType instanceof ClassType && rightType instanceof ClassType) {
//                 return new BoolType();
//             } else {
//                 throw new TypeErrorException("Type error: cannot compare " + leftType + " and " + rightType);
//             }
//         } else {
//             throw new TypeErrorException("Unknown operator " + exp.op);
//         }
//     }

//     public static Type typeof(final Exp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
//         if(exp instanceof TrueExp){ 
//             return new BoolType();
//         } else if(exp instanceof FalseExp) {
//             return new BoolType();
//         } else if(exp instanceof IntegerLiteralExp) {
//             return new IntType();
//         } else if(exp instanceof VariableExp) {
//             final Variable variable = ((VariableExp) exp).variable;
//             final Type type = typeEnvironment.get(variable);
//             if(type == null) {
//                 throw new TypeErrorException("Type error: variable " + variable + " is out of scope");
//             } else {
//                 return type;
//             }
//         } else if(exp instanceof OpExp) {
//             return typeofOpExp((OpExp) exp, typeEnvironment);
//         } else if(exp instanceof MethodCallExp) {
//             return typeofMethodCall((MethodCallExp) exp, typeEnvironment);
//         }
//         throw new TypeErrorException("Unknown expression " + exp);
//     } 
    
// }

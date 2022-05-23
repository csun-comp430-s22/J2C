package typechecker;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.*;

public class TypeChecker {

    public static final String BASE_CLASS_NAME = "Object";
    public final Map<ClassName, ClassDef> classes;
    public final Map<ClassName, DuplicateMapper<MethodName, MethodDef>> methods;
    public final Program program;

    public static ClassDef getClass(final ClassName className, final Map<ClassName, ClassDef> classes)
            throws TypeErrorException {
        if (className.value.equals(BASE_CLASS_NAME)) {
            return null;
        } else {
            final ClassDef classDef = classes.get(className);
            if (classDef == null) {
                throw new TypeErrorException("Class " + className.value
                        + " is not defined");

            } else {
                return classDef;
            }
        }
    }

    public ClassDef getClass(final ClassName className) throws TypeErrorException {
        return getClass(className, classes);
    }

    public static ClassDef getParentOf(final ClassName className, final Map<ClassName, ClassDef> classes)
            throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        return getClass(classDef.extendsClassName, classes);
    }

    public ClassDef getParentOf(final ClassName className) throws TypeErrorException {
        return getParentOf(className, classes);
    }

    public static void assertInheritanceNonCyclicalClass(final ClassDef classDef,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final Set<ClassName> seenClasses = new HashSet<ClassName>();
        seenClasses.add(classDef.className);
        ClassDef parentClassDef = getParentOf(classDef.className, classes);
        while (parentClassDef != null) {
            final ClassName parentClassName = parentClassDef.className;
            if (seenClasses.contains(parentClassName)) {
                throw new TypeErrorException("Class " + classDef.className.value
                        + " is not defined");
            }
            seenClasses.add(parentClassName);
            parentClassDef = getParentOf(parentClassName, classes);
        }
    }

    public static void assertInheritanceNonCyclical(final Map<ClassName, ClassDef> classes)
            throws TypeErrorException {
        for (final ClassDef classDef : classes.values()) {
            assertInheritanceNonCyclicalClass(classDef, classes);
        }
    }

    public static DuplicateMapper<MethodName, MethodDef> methodsForClass(final ClassName className,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        if (classDef == null) {
            return new DuplicateMapper<MethodName, MethodDef>();
        } else {
            final DuplicateMapper<MethodName, MethodDef> retval = methodsForClass(classDef.extendsClassName,
                    classes);
            final Map<MethodName, Integer> methodsOnThisClass = new HashMap<MethodName, Integer>();
            for (final MethodDef methodDef : classDef.methods) {
                final MethodName methodName = methodDef.mName;
                if (methodsOnThisClass.containsKey(methodName)
                        && methodsOnThisClass.containsValue(methodDef.params.size())) {
                    throw new TypeErrorException("Duplicate method found: " + methodName);
                }
                methodsOnThisClass.put(methodName, methodDef.params.size());
                retval.put(methodName, methodDef);
            }
            return retval;
        }
    }

    public static Map<ClassName, DuplicateMapper<MethodName, MethodDef>> makeMethodMapper(
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final Map<ClassName, DuplicateMapper<MethodName, MethodDef>> retval = new HashMap<ClassName, DuplicateMapper<MethodName, MethodDef>>();
        for (final ClassName className : classes.keySet()) {
            retval.put(className, methodsForClass(className, classes));
        }
        return retval;
    }

    public static Map<ClassName, ClassDef> classMapper(final List<ClassDef> classes)
            throws TypeErrorException {
        final Map<ClassName, ClassDef> retval = new HashMap<ClassName, ClassDef>();
        for (final ClassDef classDef : classes) {
            final ClassName className = classDef.className;
            if (retval.containsKey(classDef.className)) {
                throw new TypeErrorException("Duplicate class name: " + className);
            } else {
                retval.put(className, classDef);
            }
        }
        assertInheritanceNonCyclical(retval);
        return retval;
    }

    public TypeChecker(final Program program) throws TypeErrorException {
        this.program = program;
        classes = classMapper(program.classDefs);
        methods = makeMethodMapper(classes);
    }

    public static Type typeOfVariable(final VariableExp exp, final Map<Variable, Type> typeEnvironment)
            throws TypeErrorException {
        final Type mapType = typeEnvironment.get(exp.variable);
        if (mapType == null) {
            throw new TypeErrorException("Variable " + exp.variable.name
                    + " is not defined");
        } else {
            return mapType;
        }
    }

    public Type typeOfOp(final OpExp exp, final Map<Variable, Type> typeEnvironment, final ClassName classWeAreIn)
            throws TypeErrorException {
        final Type leftType = typeOf(exp.left, typeEnvironment, classWeAreIn);
        final Type rightType = typeOf(exp.right, typeEnvironment, classWeAreIn);
        if ((exp.op instanceof MultiplicationOp) || (exp.op instanceof DivisionOp) || (exp.op instanceof PlusOp)
                || (exp.op instanceof SubtractionOp)) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operator " + exp.op.toString()
                        + " cannot be applied to types " + leftType.toString()
                        + " and " + rightType.toString());
            }
        } else if ((exp.op instanceof LessThanOp) || (exp.op instanceof GreaterThanOp)
                || (exp.op instanceof EqualsOp) || (exp.op instanceof NotEqualsOp)) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Only integer operands allowed for comparison operations. Given: "
                        + leftType + " and " + rightType);
            }
        } else {
            throw new TypeErrorException("Unknown op: " + exp.op);
        }

    }

    public MethodDef gMethodDef(final ClassName className, final MethodName methodName, final int numOfParams)
            throws TypeErrorException {
        final DuplicateMapper<MethodName, MethodDef> methodMap = methods.get(className);
        if (methodMap == null) {
            throw new TypeErrorException("Unknown class name: " + className);
        } else {
            List<MethodDef> defs = methodMap.get(methodName);
            if (defs == null) {
                throw new TypeErrorException("Unknown method name: " + methodName + " for class " + className + " with "
                        + numOfParams + " params");
            }
            for (final MethodDef def : defs) {
                if (def.params.size() == numOfParams) {
                    return def;
                }
            }
            throw new TypeErrorException("Unknown method name: " + methodName + " for class " + className + " with "
                    + numOfParams + " params");
        }
    }

    public Type expectedReturnTypeForClassAndMethod(final ClassName className, final MethodName methodName,
            final int numOfParams)
            throws TypeErrorException {
        return gMethodDef(className, methodName, numOfParams).returnType;
    }

    public List<Type> expectedConstructorTypesForClass(final ClassName className) throws TypeErrorException {
        final ClassDef classDef = getClass(className);
        final List<Type> retval = new ArrayList<Type>();
        if (classDef == null) {
            return retval;
        } else {
            for (final Param param : classDef.params) {
                retval.add(param.paramType);
            }
            return retval;
        }
    }

    public List<Type> expectedParamTypesForClassAndMethod(final ClassName className, final MethodName methodName,
            final int numOfParams) throws TypeErrorException {
        final MethodDef methodDef = gMethodDef(className, methodName, numOfParams);
        final List<Type> retval = new ArrayList<Type>();
        for (final Param param : methodDef.params) {
            retval.add(param.paramType);
        }
        return retval;
    }

    public void assertEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
        if (first.equals(second)) {
            return;
        } else if (first instanceof ClassNameType && second instanceof ClassNameType) {
            final ClassDef parentClassDef = getParentOf(((ClassNameType) first).className);
            assertEqualOrSubtypeOf(new ClassNameType(parentClassDef.className), second);
        } else {
            throw new TypeErrorException("Type " + first.toString() + " is not equal to type " + second.toString());
        }
    }

    public void expressionsOk(final List<Type> expectedTypes, final List<Exp> receivedExpressions,
            final Map<Variable, Type> typeEnvironment, final ClassName classWeAreIn) throws TypeErrorException {
        if (expectedTypes.size() != receivedExpressions.size()) {
            throw new TypeErrorException("Expected " + expectedTypes.size() + " expressions, but received "
                    + receivedExpressions.size());

        }
        for (int index = 0; index < expectedTypes.size(); index++) {
            final Type paramType = typeOf(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
            final Type expectedType = expectedTypes.get(index);
            assertEqualOrSubtypeOf(paramType, expectedType);
        }
    }

    public Type typeOfMethodCall(final VarMethodCallExp exp, final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type varNameType = typeOf(exp.var, typeEnvironment, classWeAreIn);
        if (varNameType instanceof ClassNameType) {
            final ClassName className = ((ClassNameType) varNameType).className;
            final List<Type> expectedTypes = expectedParamTypesForClassAndMethod(className, exp.methodName.methodName,
                    exp.args.size());
            return expectedReturnTypeForClassAndMethod(className, exp.methodName.methodName, exp.args.size());
        } else {
            throw new TypeErrorException("Called method on non-class type: " + varNameType);
        }
    }

    public Type typeOfNew(final NewClassNameExp exp, final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {

        final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className.className);
        expressionsOk(expectedTypes, exp.args, typeEnvironment, classWeAreIn);
        return new ClassNameType(exp.className.className);
    }

    public Type typeOf(final Exp exp, final Map<Variable, Type> typeEnvironment, final ClassName classWeAreIn)
            throws TypeErrorException {
        if (exp instanceof TrueExp) {
            return new BoolType();
        } else if (exp instanceof FalseExp) {
            return new BoolType();
        } else if (exp instanceof IntegerLiteralExp) {
            return new IntType();
        } else if (exp instanceof StringLiteralExp) {
            return new StringType();
        } else if (exp instanceof ClassNameExp) {
            return new ClassNameType(((ClassNameExp) exp).className);
        } else if (exp instanceof VariableExp) {
            return typeOfVariable((VariableExp) exp, typeEnvironment);
        } else if (exp instanceof OpExp) {
            return typeOfOp((OpExp) exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof VarMethodCallExp) {
            return typeOfMethodCall((VarMethodCallExp) exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof NewClassNameExp) {
            return typeOfNew((NewClassNameExp) exp, typeEnvironment, classWeAreIn);
        } else {
            throw new TypeErrorException("Unrecognized expression: " + exp);
        }
    }

    public static Map<Variable, Type> addToMap(final Map<Variable, Type> map,
            final Variable variable,
            final Type type) {
        final Map<Variable, Type> result = new HashMap<Variable, Type>();
        result.putAll(map);
        result.put(variable, type);
        return result;
    }

    public Map<Variable, Type> isWellTypedStmt(final Stmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (stmt instanceof VarDecStmt) {
            final VarDecStmt tempStmt = (VarDecStmt) (stmt);
            final VariableDeclaration vardec = tempStmt.varDec.result;
            return isWellTypedVar(vardec, typeEnvironment, classWeAreIn);
        } else if (stmt instanceof VarAssignStmt) {
            return isWellTypedVarAssign((VarAssignStmt) stmt, typeEnvironment, classWeAreIn);
        } else if (stmt instanceof WhileStmt) {
            return isWellTypedWhile((WhileStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof IfStmt) {
            return isWellTypedIf((IfStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof ReturnStmt) {
            return isWellTypedReturn((ReturnStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof BlockStmt) {
            return isWellTypedBlockStmt((BlockStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof PrintlnStmt) {
            return isWellTypedPrint((PrintlnStmt) stmt, typeEnvironment, classWeAreIn);
        } else if (stmt instanceof ExpStmt) {
            typeOf(((ExpStmt) stmt).exp, typeEnvironment, classWeAreIn);
            return typeEnvironment;
        } else if (stmt instanceof ThisStmt) {
            return isWellTypedThis((ThisStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else {
            throw new TypeErrorException("Unsupported statement: " + stmt);
        }
    }

    public Map<Variable, Type> isWellTypedVar(final VariableDeclaration stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type expType = typeOf(stmt.init, typeEnvironment, classWeAreIn);
        assertEqualOrSubtypeOf(expType, stmt.type);
        return addToMap(typeEnvironment, ((VariableExp) stmt.id).variable, stmt.type);
    }

    public Map<Variable, Type> isWellTypedVarAssign(final VarAssignStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type varType = typeOf(stmt.id, typeEnvironment, classWeAreIn);
        final Type expType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
        assertEqualOrSubtypeOf(expType, varType);
        return typeEnvironment;
    }

    public Map<Variable, Type> isWellTypedWhile(final WhileStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeOf(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            isWellTypedStmt(stmt.body, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("Guard of while statement is not a boolean");

        }
    }

    public Map<Variable, Type> isWellTypedIf(final IfStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeOf(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            isWellTypedStmt(stmt.trueBranch, typeEnvironment, classWeAreIn, functionReturnType);
            isWellTypedStmt(stmt.falseBranch, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {

            throw new TypeErrorException("Guard of if statement is not a boolean");

        }
    }

    public Map<Variable, Type> isWellTypedReturn(final ReturnStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (functionReturnType == null) {

            throw new TypeErrorException("Return statement in void function");

        } else {
            final Type receivedType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
            assertEqualOrSubtypeOf(receivedType, functionReturnType);
            return typeEnvironment;
        }
    }

    public Map<Variable, Type> isWellTypedBlockStmt(final BlockStmt stmt,
            Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        for (final Stmt bodyStmt : stmt.stmts) {
            typeEnvironment = isWellTypedStmt(bodyStmt, typeEnvironment, classWeAreIn, functionReturnType);
        }
        return typeEnvironment;
    }

    public Map<Variable, Type> isWellTypedPrint(final PrintlnStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        for (final Exp printExp : stmt.exps) {
            typeOf(printExp, typeEnvironment, classWeAreIn);
        }
        return typeEnvironment;
    }

    public boolean isWellTypedSuperParamsToVarType(ClassDef superclass, Exp variable,
            Map<Variable, Type> typeEnviornment, ClassName classWeAreIn) throws TypeErrorException {
        Type varType = typeOf(variable, typeEnviornment, classWeAreIn);
        boolean typeIsAMatch = false;
        for (Param parameterType : superclass.params) {
            if (parameterType.paramType.equals(varType)) {
                typeIsAMatch = true;
            }
        }

        return typeIsAMatch;
    }

    public Map<Variable, Type> isWellTypedThis(final ThisStmt var, final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn, final Type ReturnType) throws TypeErrorException {
        if ((typeEnvironment.containsKey(var.id.variable)) && (typeEnvironment.containsKey(var.next.variable))) {
            Type LeftSideType = typeEnvironment.get(var.id.variable);
            Type RightSideType = typeEnvironment.get(var.next.variable);
            if (LeftSideType.equals(RightSideType) == true) {
                return typeEnvironment;
            } else {
                throw new TypeErrorException("Type of this is not the same as the type of the variable");
            }

        } else {
            throw new TypeErrorException("This is not a valid variable");
        }

    }

    public void isWellTypedMethodDef(final MethodDef method,
            Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Set<Variable> variablesInMethod = new HashSet<Variable>();
        for (final Param param : method.params) {
            final VariableExp variableExp = ((VariableExp) param.var);
            final Variable variable = variableExp.variable;
            if (variablesInMethod.contains(variable)) {
                throw new TypeErrorException("Duplicate variable in method definition: " + variable);
            }
            variablesInMethod.add(variable);
            typeEnvironment = addToMap(typeEnvironment, ((VariableExp) param.var).variable, param.paramType);
        }
        isWellTypedStmt(method.body, typeEnvironment, classWeAreIn, method.returnType);
    }

    public Map<Variable, Type> baseTypeEnvironmentForClass(final ClassName className) throws TypeErrorException {
        final ClassDef classDef = getClass(className);
        if (classDef == null) {
            return new HashMap<Variable, Type>();
        } else {
            final Map<Variable, Type> retval = baseTypeEnvironmentForClass(classDef.extendsClassName);
            for (final VariableDeclaration instanceVariable : classDef.varDecs) {
                final VariableExp variableExp = ((VariableExp) instanceVariable.id);
                final Variable variable = variableExp.variable;
                if (retval.containsKey(variable)) {
                    throw new TypeErrorException("Duplicate variable in class definition: " + variable);
                }
                retval.put(variable, instanceVariable.type);
            }
            return retval;
        }
    }

    public void isWellTypedClassDef(final ClassDef classDef) throws TypeErrorException {
        final Map<Variable, Type> typeEnvironment = baseTypeEnvironmentForClass(classDef.className);
        Map<Variable, Type> constructorTypeEnvironment = typeEnvironment;
        final Set<Variable> variablesInConstructor = new HashSet<Variable>();
        for (final Param param : classDef.params) {
            final VariableExp variableExp = ((VariableExp) param.var);
            final Variable variable = variableExp.variable;
            if (variablesInConstructor.contains(variable)) {
                throw new TypeErrorException("Duplicate variable in constructor param: " + variable);
            }
            variablesInConstructor.add(variable);
            constructorTypeEnvironment = addToMap(constructorTypeEnvironment, variable, param.paramType);
        }
        isWellTypedStmt(classDef.body, constructorTypeEnvironment, classDef.className, null);
        for (final MethodDef method : classDef.methods) {
            isWellTypedMethodDef(method, typeEnvironment, classDef.className);
        }
    }

    public void isWellTypedProgram() throws TypeErrorException {
        for (final ClassDef classDef : program.classDefs) {
            isWellTypedClassDef(classDef);
        }
        for (final Stmt stmt : program.stmts) {
            isWellTypedStmt(stmt, new HashMap<Variable, Type>(), null, null);
        }
    }

}

// public static Type typeofMethodCall(final MethodCallExp exp, final
// Map<Variable, Type> typeEnvironment) throws TypeErrorException {

// }

// public static Type typeofOpExp(final OpExp exp, final Map<Variable, Type>
// typeEnvironment) throws TypeErrorException {
// final Type leftType = typeof(exp.left, typeEnvironment);
// final Type rightType = typeof(exp.right, typeEnvironment);
// if(exp.op instanceof PlusOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new IntType();
// } else {
// throw new TypeErrorException("Type error: cannot add " + leftType + " and " +
// rightType);
// }
// } else if (exp.op instanceof SubtractionOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new IntType();
// } else {
// throw new TypeErrorException("Type error: cannot subtract " + leftType + "
// and " + rightType);
// }
// } else if (exp.op instanceof MultiplicationOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new IntType();
// } else {
// throw new TypeErrorException("Type error: cannot multiply " + leftType + "
// and " + rightType);
// }
// } else if (exp.op instanceof DivisionOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new IntType();
// } else {
// throw new TypeErrorException("Type error: cannot divide " + leftType + " and
// " + rightType);
// }
// } else if (exp.op instanceof LessThanOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new BoolType();
// } else {
// throw new TypeErrorException("Type error: cannot compare " + leftType + " and
// " + rightType);
// }
// } else if(exp.op instanceof GreaterThanOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new BoolType();
// } else {
// throw new TypeErrorException("Type error: cannot compare " + leftType + " and
// " + rightType);
// }
// } else if(exp.op instanceof EqualsOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new BoolType();
// } else if(leftType instanceof BoolType && rightType instanceof BoolType) {
// return new BoolType();
// } else if(leftType instanceof StringType && rightType instanceof StringType)
// {
// return new BoolType();
// } else if(leftType instanceof ClassType && rightType instanceof ClassType) {
// return new BoolType();
// } else {
// throw new TypeErrorException("Type error: cannot compare " + leftType + " and
// " + rightType);
// }
// } else if(exp.op instanceof NotEqualsOp) {
// if(leftType instanceof IntType && rightType instanceof IntType) {
// return new BoolType();
// } else if(leftType instanceof BoolType && rightType instanceof BoolType) {
// return new BoolType();
// } else if(leftType instanceof StringType && rightType instanceof StringType)
// {
// return new BoolType();
// } else if(leftType instanceof ClassType && rightType instanceof ClassType) {
// return new BoolType();
// } else {
// throw new TypeErrorException("Type error: cannot compare " + leftType + " and
// " + rightType);
// }
// } else {
// throw new TypeErrorException("Unknown operator " + exp.op);
// }
// }

// public static Type typeof(final Exp exp, final Map<Variable, Type>
// typeEnvironment) throws TypeErrorException {
// if(exp instanceof TrueExp){
// return new BoolType();
// } else if(exp instanceof FalseExp) {
// return new BoolType();
// } else if(exp instanceof IntegerLiteralExp) {
// return new IntType();
// } else if(exp instanceof VariableExp) {
// final Variable variable = ((VariableExp) exp).variable;
// final Type type = typeEnvironment.get(variable);
// if(type == null) {
// throw new TypeErrorException("Type error: variable " + variable + " is out of
// scope");
// } else {
// return type;
// }
// } else if(exp instanceof OpExp) {
// return typeofOpExp((OpExp) exp, typeEnvironment);
// } else if(exp instanceof MethodCallExp) {
// return typeofMethodCall((MethodCallExp) exp, typeEnvironment);
// }
// throw new TypeErrorException("Unknown expression " + exp);
// }

// }

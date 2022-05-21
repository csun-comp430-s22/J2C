package typechecker;
import parser.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeCheckerTest {

    @Test
	public void testGetClass() throws TypeErrorException {
		final ClassDef expected = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		map.put(classname, expected);
		final ClassDef received = TypeChecker.getClass(classname, map);
		assertEquals(expected, received);
	}


    @Test
    public void testGetClass2() throws TypeErrorException {
        final ClassDef expected = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        final ClassName classname = new ClassName("Foo");
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        map.put(classname, expected);
        final ClassDef received = TypeChecker.getClass(classname, map);
        assertEquals(expected, received);
    }

    @Test
    public void testGetClass3() throws TypeErrorException {
        final ClassDef expected = new ClassDef(new ClassName("Object"), new ClassName(""),
            new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
            new ArrayList<MethodDef>());
    final ClassName classname = new ClassName("Object");
    final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
    map.put(classname, expected);
    final ClassDef received = TypeChecker.getClass(classname, map);
    assertEquals(null, received);
    }

    @Test
    public void testGetClass4() throws TypeErrorException {
        final ClassDef expected = new ClassDef(new ClassName("Object"), new ClassName(""),
            new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
            new ArrayList<MethodDef>());
    final ClassName classname = new ClassName("Object");
    final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
    map.put(classname, expected);
    final ClassDef received = TypeChecker.getClass(classname, map);
    assertEquals(null, received);
    }



    @Test
	public void testGetParentOf() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		typechecker.classes.put(classname, classDef);
		final ClassName parentClassname = new ClassName("Bar");
		final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(parentClassname, parentClassDef);
		final ClassDef received = typechecker.getParentOf(classname);
		assertEquals(parentClassDef, received);
    }

    @Test
    public void testGetParentOf2() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        final ClassName classname = new ClassName("Foo");
        typechecker.classes.put(classname, classDef);
        final ClassName parentClassname = new ClassName("Bar");
        final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Object"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        typechecker.classes.put(parentClassname, parentClassDef);
        final ClassDef received = typechecker.getParentOf(classname);
        assertEquals(parentClassDef, received);
    }

    @Test
    public void testAssertInheritanceNonCyclicalClass() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final ClassName parentClassname = new ClassName("Bar");
		final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		TypeChecker.assertInheritanceNonCyclicalClass(classDef, map);
    }

    @Test
    public void testMethodsForClass() throws TypeErrorException {
        final ClassName className = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		methodDefs.add(methodDef);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		map.put(className, classDef);
		final DuplicateMapper<MethodName, MethodDef> expected = new DuplicateMapper<MethodName, MethodDef>();
		expected.put(methodDef.mName, methodDef);
		final DuplicateMapper<MethodName, MethodDef> received = TypeChecker.methodsForClass(className, map);
		assertEquals(expected, received);
    }

    @Test
    public void testMethodsForClass2() throws TypeErrorException {
        final ClassName className = new ClassName("Foo");
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        final List<Param> methodParams = new ArrayList<Param>();
        methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
        final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
                new ExpStmt(new IntegerLiteralExp(0)));
        final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(methodDef);
        final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                methodDefs);
        map.put(className, classDef);
        final DuplicateMapper<MethodName, MethodDef> expected = new DuplicateMapper<MethodName, MethodDef>();
        expected.put(methodDef.mName, methodDef);
        final DuplicateMapper<MethodName, MethodDef> received = TypeChecker.methodsForClass(className, map);
        assertEquals(expected, received);
    }

    @Test
    public void testMethodsForClass3() throws TypeErrorException { 
        final ClassName className = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final List<Param> methodParams = new ArrayList<Param>();
		final List<Param> methodParams2 = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Param(new IntType(), new VariableExp(new Variable("y"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final MethodDef methodDef2 = new MethodDef(new IntType(), new MethodName("doSomething"),
				methodParams2, new ExpStmt(new IntegerLiteralExp(0)));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		map.put(className, classDef);
		final DuplicateMapper<MethodName, MethodDef> expected = new DuplicateMapper<MethodName, MethodDef>();
		expected.put(methodDef.mName, methodDef);
		expected.put(methodDef2.mName, methodDef2);
		final DuplicateMapper<MethodName, MethodDef> received = TypeChecker.methodsForClass(className, map);
		assertEquals(expected, received);
    }

    @Test
    public void testMethodsForClass4() throws TypeErrorException {
        final ClassName className = new ClassName("Foo");
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        final List<Param> methodParams = new ArrayList<Param>();
        final List<Param> methodParams2 = new ArrayList<Param>();
        methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
        methodParams2.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
        methodParams2.add(new Param(new IntType(), new VariableExp(new Variable("y"))));
        final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
                new ExpStmt(new IntegerLiteralExp(0)));
        final MethodDef methodDef2 = new MethodDef(new IntType(), new MethodName("doSomething"),
                methodParams2, new ExpStmt(new IntegerLiteralExp(0)));
        final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(methodDef);
        methodDefs.add(methodDef2);
        final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                methodDefs);
        map.put(className, classDef);
        final DuplicateMapper<MethodName, MethodDef> expected = new DuplicateMapper<MethodName, MethodDef>();
        expected.put(methodDef.mName, methodDef);
        expected.put(methodDef2.mName, methodDef2);
        final DuplicateMapper<MethodName, MethodDef> received = TypeChecker.methodsForClass(className, map);
        assertEquals(expected, received);
    }

    @Test
	public void testMakeMethodMapper() throws TypeErrorException {

		final ClassName className = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		methodDefs.add(methodDef);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		map.put(className, classDef);
		final Map<ClassName, DuplicateMapper<MethodName, MethodDef>> expected = new HashMap<ClassName, DuplicateMapper<MethodName, MethodDef>>();
		final DuplicateMapper<MethodName, MethodDef> map2 = new DuplicateMapper<MethodName, MethodDef>();
		map2.put(new MethodName("doSomething"), methodDef);
		expected.put(className, map2);
		final Map<ClassName, DuplicateMapper<MethodName, MethodDef>> received = TypeChecker.makeMethodMapper(map);
		assertEquals(expected, received);
	}

    @Test
	public void testClassMapper() throws TypeErrorException {
		final List<ClassDef> classDefs = new ArrayList<ClassDef>();
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		classDefs.add(classDef);
		final ClassName classname = new ClassName("Foo");
		final Map<ClassName, ClassDef> expected = new HashMap<ClassName, ClassDef>();
		expected.put(classname, classDef);
		final Map<ClassName, ClassDef> received = TypeChecker.classMapper(classDefs);
		assertEquals(expected, received);
	}

    @Test
    public void testgetClass() throws TypeErrorException {
        final ClassName className = new ClassName("Foo");
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        final List<Param> methodParams = new ArrayList<Param>();
        methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
        final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
                new ExpStmt(new IntegerLiteralExp(0)));
        final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(methodDef);
        final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                methodDefs);
        map.put(className, classDef);
        final ClassDef received = TypeChecker.getClass(className, map);
        assertEquals(classDef, received);
    }

    @Test
    public void testTypeOfVariable() throws TypeErrorException {
        final ExpStmt expStmt = new ExpStmt(new IntegerLiteralExp(0));
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(expStmt);
		final Type expectedType = new IntType();
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type receivedType = TypeChecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
		assertEquals(expectedType, receivedType);
		
    }

    @Test
    public void testTypeOfOpMultiplication() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerLiteralExp(0), new MultiplicationOp(), new IntegerLiteralExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
		assertEquals(expected, received);
	}

    @Test
	public void testTypeOfOpDivision() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(0), new DivisionOp(), new IntegerLiteralExp(1));
        final Type expected = new IntType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testTypeOfOpAddition() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(10), new PlusOp(), new IntegerLiteralExp(1));
        final Type expected = new IntType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testTypeOfOpSubtraction() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(5), new SubtractionOp(), new IntegerLiteralExp(2));
        final Type expected = new IntType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testTypeOfOpLessThan() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(5), new LessThanOp(), new IntegerLiteralExp(2));
        final Type expected = new BoolType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
    public void testTypeOfOpGreaterThan() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(5), new GreaterThanOp(), new IntegerLiteralExp(2));
        final Type expected = new BoolType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testTypeOfOpEquals() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(5), new EqualsOp(), new IntegerLiteralExp(5));
        final Type expected = new BoolType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testTypeOfOpNotEquals() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final OpExp exp = new OpExp(new IntegerLiteralExp(5), new NotEqualsOp(), new IntegerLiteralExp(5));
        final Type expected = new BoolType();
        final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
        assertEquals(expected, received);
    }

    @Test
	public void testExpectedReturnTypeForClassAndMethod() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("doSomething");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		final Type expected = methodDef.returnType;
		final Type received = typechecker.expectedReturnTypeForClassAndMethod(className, methodName,
				methodParams.size());
		assertEquals(expected, received);
	}

    @Test
	public void testExpectedParamTypesForClassAndMethod() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("findNum");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("findNum"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		final List<Type> expected = new ArrayList<Type>();
		expected.add(methodParams.get(0).paramType);

		final List<Type> received = typechecker.expectedParamTypesForClassAndMethod(className, methodName,
				methodParams.size());
		assertEquals(expected, received);
	}

    @Test
	public void testGMethodDef() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("doSomething");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		final MethodDef expected = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final MethodDef received = typechecker.gMethodDef(className, methodName, methodParams.size());
		assertEquals(expected, received);
	}

    @Test
	public void testGMethodDefOverloaded() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("doSomething");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), methodName, methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		final List<Param> method2Params = new ArrayList<Param>();
		method2Params.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		method2Params.add(new Param(new BoolType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef2 = new MethodDef(new IntType(), methodName, method2Params,
				new ExpStmt(new IntegerLiteralExp(0)));
		methodsForClass.put(methodName, methodDef2);
		typechecker.methods.put(className, methodsForClass);
		final MethodDef expected = methodDef;
		final MethodDef received = typechecker.gMethodDef(className, methodName, methodParams.size());
		assertEquals(expected, received);
    }

    @Test
	public void testExpectedConstructorTypes() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Object");
		final List<Type> expected = new ArrayList<Type>();
		final List<Type> received = typechecker.expectedConstructorTypesForClass(className);
		assertEquals(expected, received);
	}

	@Test
	public void testExpectedConstructorTypes2()
			throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final List<Param> params = new ArrayList<Param>();
		params.add(new Param(new StringType(), new VariableExp(new Variable("value"))));
		final ClassDef classDef = new ClassDef(className, new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(className, classDef);
		final List<Type> expected = new ArrayList<Type>();
		expected.add(new StringType());
		final List<Type> received = typechecker.expectedConstructorTypesForClass(className);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfNew() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final ClassName className = new ClassName("Foo");
		final List<Param> params = new ArrayList<Param>();
		params.add(new Param(new StringType(), new VariableExp(new Variable("value"))));
		final ClassDef classDef = new ClassDef(className, new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassNameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		newClassParams.add(new StringLiteralExp("Hello World"));
		final NewClassNameExp newClassExp = new NewClassNameExp(new ClassNameExp(className), newClassParams);
		final Type received = typechecker.typeOfNew(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfNew2() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final ClassName className = new ClassName("Foo");
		final List<Param> params = new ArrayList<Param>();
		final ClassDef classDef = new ClassDef(className, new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassNameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		final NewClassNameExp newClassExp = new NewClassNameExp(new ClassNameExp(className), newClassParams);
		final Type received = typechecker.typeOfNew(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}

    @Test
	public void testExpressionsOk() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final List<Type> expectedTypes = new ArrayList<Type>();
		final List<Exp> receivedExps = new ArrayList<Exp>();
		expectedTypes.add(new IntType());
		receivedExps.add(new IntegerLiteralExp(0));
		typechecker.expressionsOk(expectedTypes, receivedExps, new HashMap<Variable, Type>(),
				new ClassName("Lorem Ipsum Dolor Sit Amet"));
	}

    @Test
	public void testTypeOfMethodCall() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		final MethodDef methodDef = new MethodDef(new StringType(), new MethodName("run"),
				new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)));
		methodDefs.add(methodDef);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		final ClassName classname = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		map.put(classname, classDef);
		typechecker.classes.put(classname, classDef);
		final DuplicateMapper<MethodName, MethodDef> theMethods = new DuplicateMapper<MethodName, MethodDef>();
		theMethods.put(new MethodName("run"), methodDef);
		typechecker.methods.put(classname, theMethods);
		final List<Exp> inParens = new ArrayList<Exp>();
		final VarMethodCallExp methodCall = new VarMethodCallExp(new ClassNameExp(new ClassName("Foo")),
				new MethodNameExp(new MethodName("run")), inParens);
		final Type expected = new StringType();
		final Type received = typechecker.typeOfMethodCall(methodCall, new HashMap<Variable, Type>(),
				new ClassName("Foo"));
		assertEquals(expected, received);
	}

    @Test
	public void testAssertEqualOrSubtype() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		typechecker.classes.put(classname, classDef);
		final ClassName parentClassname = new ClassName("Bar");
		final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(parentClassname, parentClassDef);
		typechecker.assertEqualOrSubtypeOf(new ClassNameType(classname), new ClassNameType(parentClassname));
	}

    @Test
	public void testAssertEqualOrSubtype2() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new IntType(), new IntType());
	}

    @Test
	public void testTypeOfTrueExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Type expected = new BoolType();
		final Type received = typechecker.typeOf(new TrueExp(), null, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfFalseExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Type expected = new BoolType();
		final Type received = typechecker.typeOf(new FalseExp(), null, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfIntegerLiteralExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new IntegerLiteralExp(0), null, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfStringLiteralExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Type expected = new StringType();
		final Type received = typechecker.typeOf(new StringLiteralExp("Hello COMP430"), null, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfClassNameExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Type expected = new ClassNameType(new ClassName("Foo"));
		final Type received = typechecker.typeOf(new ClassNameExp(new ClassName("Foo")), null, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfVariableExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new VariableExp(new Variable("x")), typeEnvironment, null);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfOpExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final ClassName classname = new ClassName("Lorem ipsum");
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new OpExp(new IntegerLiteralExp(0), new PlusOp(), new IntegerLiteralExp(1)),
				typeEnvironment, classname);
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfVarMethodCall() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		final MethodDef methodDef = new MethodDef(new StringType(), new MethodName("run"),
				new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)));
		methodDefs.add(methodDef);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		final ClassName classname = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		map.put(classname, classDef);
		typechecker.classes.put(classname, classDef);
		final DuplicateMapper<MethodName, MethodDef> theMethods = new DuplicateMapper<MethodName, MethodDef>();
		theMethods.put(new MethodName("run"), methodDef);
		typechecker.methods.put(classname, theMethods);
		final List<Exp> inParens = new ArrayList<Exp>();
		final VarMethodCallExp methodCall = new VarMethodCallExp(new ClassNameExp(new ClassName("Foo")),
				new MethodNameExp(new MethodName("run")), inParens);
		final Type expected = new StringType();
		final Type received = typechecker.typeOf(methodCall, new HashMap<Variable, Type>(), new ClassName("Foo"));
		assertEquals(expected, received);
	}

	@Test
	public void testTypeOfNewClassExp() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final ClassName className = new ClassName("Foo");
		final List<Param> params = new ArrayList<Param>();
		final ClassDef classDef = new ClassDef(className, new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassNameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		final NewClassNameExp newClassExp = new NewClassNameExp(new ClassNameExp(className), newClassParams);
		final Type received = typechecker.typeOf(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}



















    // Testing exceptions 

    @Test(expected = TypeErrorException.class)
	public void testGetClassException() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
            new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
    final ClassName classname = new ClassName("Foo");
    final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
    final ClassDef received = TypeChecker.getClass(classname, map);
    }

    @Test(expected = TypeErrorException.class)
    public void testGetClassException2() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
            new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
    final ClassName classname = new ClassName("Bar");
    final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
    final ClassDef received = TypeChecker.getClass(classname, map);
    }

    @Test(expected = TypeErrorException.class)
    public void testAssertInheritanceNonCyclicalClassException() throws TypeErrorException {

        final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final ClassName parentClassname = new ClassName("Bar");
		final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Foo"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		TypeChecker.assertInheritanceNonCyclicalClass(classDef, map);
        
    }

    @Test(expected = TypeErrorException.class)
    public void testAssertInheritanceNonCyclicalClassException2() throws TypeErrorException {

        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        final ClassName classname = new ClassName("Foo");
        typechecker.classes.put(classname, classDef);
        map.put(classname, classDef);
        final ClassName parentClassname = new ClassName("Bar");
        final ClassDef parentClassDef = new ClassDef(new ClassName("Bar"), new ClassName("Foo"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        typechecker.classes.put(parentClassname, parentClassDef);
        map.put(parentClassname, parentClassDef);
        TypeChecker.assertInheritanceNonCyclicalClass(parentClassDef, map);
    }

    @Test (expected = TypeErrorException.class)
    public void testMethodsForClassException() throws TypeErrorException {
        final ClassName className = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		final List<Param> methodParams = new ArrayList<Param>();
		final List<Param> methodParams2 = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), new MethodName("doSomething"), methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final MethodDef methodDef2 = new MethodDef(new IntType(), new MethodName("doSomething"),
				methodParams2, new ExpStmt(new IntegerLiteralExp(0)));
		final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				methodDefs);
		map.put(className, classDef);
		TypeChecker.methodsForClass(className, map);
    }

    @Test(expected = TypeErrorException.class)
	public void testClassMapperException() throws TypeErrorException {
		final List<ClassDef> classDefs = new ArrayList<ClassDef>();
		final ClassDef classDef = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassDef classDef2 = new ClassDef(new ClassName("Foo"), new ClassName("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		classDefs.add(classDef);
		classDefs.add(classDef2);
		TypeChecker.classMapper(classDefs);
	}

    @Test(expected = TypeErrorException.class)
	public void testTypeOfOpException() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new StringLiteralExp("example"), new SubtractionOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
	}

	@Test(expected = TypeErrorException.class)
	public void testTypeOfOpException2() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerLiteralExp(1), new SubtractionOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Baz"));
	}

    @Test(expected = TypeErrorException.class)
	public void typeOfOpException3() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerLiteralExp(1), new NotEqualsOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Test"));
	}

	@Test(expected = TypeErrorException.class)
	public void typeOfOpException4() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new StringLiteralExp("Hello world"), new NotEqualsOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new ClassName("Test"));
	}


    @Test(expected = TypeErrorException.class)
	public void testGMethodDefException() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		typechecker.gMethodDef(new ClassName("Unknown Class Name"), null, 0);
	}

    @Test(expected = TypeErrorException.class)
	public void testGMethodDefException2() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("doSomething");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), methodName, methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		typechecker.gMethodDef(className, new MethodName("otherMethod"), 1);
	}

	@Test(expected = TypeErrorException.class)
	public void testGMethodDefException3() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassName className = new ClassName("Foo");
		final MethodName methodName = new MethodName("doSomething");
		final List<Param> methodParams = new ArrayList<Param>();
		methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDef methodDef = new MethodDef(new IntType(), methodName, methodParams,
				new ExpStmt(new IntegerLiteralExp(0)));
		final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		typechecker.gMethodDef(className, methodName, 2);
	}

    @Test(expected = TypeErrorException.class)
    public void testGMethodDefException4() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final ClassName className = new ClassName("Foo");
        final MethodName methodName = new MethodName("doSomething");
        final List<Param> methodParams = new ArrayList<Param>();
        methodParams.add(new Param(new IntType(), new VariableExp(new Variable("x"))));
        final MethodDef methodDef = new MethodDef(new IntType(), methodName, methodParams,
                new ExpStmt(new IntegerLiteralExp(0)));
        final DuplicateMapper<MethodName, MethodDef> methodsForClass = new DuplicateMapper<MethodName, MethodDef>();
        methodsForClass.put(methodName, methodDef);
        typechecker.methods.put(className, methodsForClass);
        typechecker.gMethodDef(className, methodName, 0);
    }

    @Test(expected = TypeErrorException.class)
	public void testTypeOfMethodCallException() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final List<Exp> inParens = new ArrayList<Exp>();
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("Foo"), new StringType());
		final VarMethodCallExp methodCall = new VarMethodCallExp(new VariableExp(new Variable("Foo")),
				new MethodNameExp(new MethodName("bar")), inParens);
		typechecker.typeOfMethodCall(methodCall, typeEnvironment, new ClassName("BAZ"));
	}

	@Test(expected = TypeErrorException.class)
	public void testExpressionsOkException() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final List<Type> expectedTypes = new ArrayList<Type>();
		final List<Exp> receivedExps = new ArrayList<Exp>();
		expectedTypes.add(new IntType());
		receivedExps.add(new IntegerLiteralExp(0));
		receivedExps.add(new TrueExp());
		typechecker.expressionsOk(expectedTypes, receivedExps, null, null);
	}

    @Test(expected = TypeErrorException.class)
	public void testAssertEqualOrSubtypeOfException() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new ClassNameType(new ClassName("Example class to test")), new IntType());
	}

	@Test(expected = TypeErrorException.class)
	public void testAssertEqualOrSubtypeOfException2() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new IntType(), new StringType());
	}

    @Test(expected = TypeErrorException.class)
	public void testTypeOfOpException3() throws TypeErrorException {
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		typechecker.typeOf(new MethodNameExp(new MethodName("TestMethod")), null, null);
	}



}

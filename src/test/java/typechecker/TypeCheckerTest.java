package typechecker;
import parser.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
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

}

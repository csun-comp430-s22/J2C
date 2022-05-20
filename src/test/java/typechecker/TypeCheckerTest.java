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
		final TypeChecker typechecker = new TypeChecker(
				new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
		final ClassDef expected = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
				new ArrayList<MethodDef>());
		final ClassName classname = new ClassName("Foo");
		final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
		map.put(classname, expected);
		final ClassDef received = typechecker.getClass(classname, map);
		assertEquals(expected, received);
	}


    @Test
    public void testGetClass2() throws TypeErrorException {
        final TypeChecker typechecker = new TypeChecker(
                new Program(new ArrayList<ClassDef>(), new ArrayList<Stmt>()));
        final ClassDef expected = new ClassDef(new ClassName("Foo"), new ClassName("Bar"),
                new ArrayList<VariableDeclaration>(), new ArrayList<Param>(), new ExpStmt(new IntegerLiteralExp(0)),
                new ArrayList<MethodDef>());
        final ClassName classname = new ClassName("Foo");
        final Map<ClassName, ClassDef> map = new HashMap<ClassName, ClassDef>();
        map.put(classname, expected);
        final ClassDef received = typechecker.getClass(classname, map);
        assertEquals(expected, received);
    }


    


}

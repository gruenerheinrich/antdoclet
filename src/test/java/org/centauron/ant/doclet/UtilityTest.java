package org.centauron.ant.doclet;

import com.sun.javadoc.MethodDoc;
import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author toben
 */
public class UtilityTest {
	
	public UtilityTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of deleteAllFilesInDir method, of class Utility.
	 */
	@Test
	public void testDeleteAllFilesInDir() {
		System.out.println("deleteAllFilesInDir");
		File dir = null;
		Utility.deleteAllFilesInDir(dir);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isMultiLine method, of class Utility.
	 */
	@Test
	public void testIsMultiLine() {
		System.out.println("isMultiLine");
		String s = "";
		boolean expResult = false;
		boolean result = Utility.isMultiLine(s);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of escapeXMLString method, of class Utility.
	 */
	@Test
	public void testEscapeXMLString() {
		System.out.println("escapeXMLString");
		String xml = "";
		String expResult = "";
		String result = Utility.escapeXMLString(xml);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of lowerFirstCharacter method, of class Utility.
	 */
	@Test
	public void testLowerFirstCharacter() {
		System.out.println("lowerFirstCharacter");
		String s = "";
		String expResult = "";
		String result = Utility.lowerFirstCharacter(s);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStringPart method, of class Utility.
	 */
	@Test
	public void testGetStringPart() {
		System.out.println("getStringPart");
		String s = "";
		int idx = 0;
		String expResult = "";
		String result = Utility.getStringPart(s, idx);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of concatArray method, of class Utility.
	 */
	@Test
	public void testConcatArray() {
		System.out.println("concatArray");
		MethodDoc[] m = null;
		MethodDoc[] m2 = null;
		MethodDoc[] expResult = null;
		MethodDoc[] result = Utility.concatArray(m, m2);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sortArray method, of class Utility.
	 */
	@Test
	public void testSortArray() {
		System.out.println("sortArray");
		MethodDoc[] m = null;
		MethodDoc[] expResult = null;
		MethodDoc[] result = Utility.sortArray(m);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}

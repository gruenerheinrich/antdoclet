package org.centauron.ant.doclet;

import com.sun.javadoc.MethodDoc;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
	 * Test of isMultiLine method, of class Utility.
	 */
	@Test
	public void testIsMultiLine() {
		assertTrue(Utility.isMultiLine("abc\ndef"));
		assertTrue(Utility.isMultiLine("\nabcdef"));
		assertFalse(Utility.isMultiLine("abcdef"));
	}

	/**
	 * Test of lowerFirstCharacter method, of class Utility.
	 */
	@Test
	public void testLowerFirstCharacter() {
		assertEquals("abc", Utility.lowerFirstCharacter("Abc"));
		assertEquals("aBC", Utility.lowerFirstCharacter("ABC"));
		assertEquals("", Utility.lowerFirstCharacter(""));
		assertNull(Utility.lowerFirstCharacter(null));
	}

	/**
	 * Test of getStringPart method, of class Utility.
	 */
	@Test
	public void testGetStringPart() {
		assertEquals("bc", Utility.getStringPart("ab bc fg", 1));
		assertEquals("ab", Utility.getStringPart("ab bc fg", 0));
		assertEquals(null, Utility.getStringPart("ab bc fg", 20));
		assertEquals("ab", Utility.getStringPart("ab bc fg", -20));
	}

	/**
	 * Test of concatArray method, of class Utility.
	 */
	@Test
	public void testConcatArray() {
		MethodDoc[] m = new MethodDoc[10];
		for (int i=9;i>=0;i--) {
			m[i]=mock(MethodDoc.class);
			when(m[i].name()).thenReturn("name" + i);
		}
		
		MethodDoc[] m2 = new MethodDoc[10];
		for (int i=0;i<10;i++) {
			m2[i]=mock(MethodDoc.class);
			when(m2[i].name()).thenReturn("name" + i);
		}
		MethodDoc[] concatArray = Utility.concatArray(m, m2);
		
		assertEquals(20, concatArray.length);
	}

	/**
	 * Test of sortArray method, of class Utility.
	 */
	@Test
	public void testSortArray() {
		System.out.println("sortArray");
		MethodDoc[] m = new MethodDoc[10];
		for (int i=9;i>=0;i--) {
			m[i]=mock(MethodDoc.class);
			when(m[i].name()).thenReturn("name" + i);
		}
		
		MethodDoc[] result = Utility.sortArray(m);
		assertEquals(m.length, result.length);
		assertEquals("name0",m[0].name());
		assertEquals("name9",m[m.length-1].name());
	}
}

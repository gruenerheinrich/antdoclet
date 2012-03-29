package org.centauron.ant.doclet;

import org.junit.*;

/**
 *
 * @author toben
 */
public class DocletTest {
	
	public DocletTest() {
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
	 * Simple start of a Sourcefile Scane.
	 */
	@Test
	public void testSimpleGeneration() {
		String[] args = new String[] {"-locale","de_DE","-doclet","org.centauron.ant.doclet.Doclet","-d","target/generated-docs/doc1","-sourcepath","src/test/resources_java/examples/src","org.centauron.ant.doclet.examples"};
		com.sun.tools.javadoc.Main.main(args);
	}
}

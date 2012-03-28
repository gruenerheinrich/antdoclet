package org.centauron.ant.doclet;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import org.junit.*;
import static org.junit.Assert.*;

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
		String[] args = new String[] {"-doclet","org.centauron.ant.doclet.Doclet","-d","target/generated-docs/doc1","-sourcepath","D:/temp/apache-ant-1.8.3-src/apache-ant-1.8.3/src/main","org.apache.tools.ant.taskdefs","org.apache.tools.ant.types"};
		com.sun.tools.javadoc.Main.main(args);
	}
}

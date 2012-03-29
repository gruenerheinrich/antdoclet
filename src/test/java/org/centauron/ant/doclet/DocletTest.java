package org.centauron.ant.doclet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
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
	public void testSimpleGenerationDe() throws FileNotFoundException, IOException {
		String[] args = new String[] {"-locale","de_DE","-doclet","org.centauron.ant.doclet.Doclet","-d","target/generated-docs/doc1_de","-sourcepath","src/test/resources_java/examples/src","org.centauron.ant.doclet.examples"};
		com.sun.tools.javadoc.Main.execute(args);
		
		File f = new File("target/generated-docs/doc1_de/cover.html");
		assertTrue(f.exists());
		FileReader reader = new FileReader(f);
		String cover = IOUtils.toString(reader);
		reader.close();
		
		assertTrue(cover.contains("Handbuch"));
		
		assertTrue(new File("target/generated-docs/doc1_de/tasks/HelloWorldChildTask.html").exists());
		assertTrue(new File("target/generated-docs/doc1_de/tasks/HelloWorldTask.html").exists());
	}
	
	@Test
	public void testSimpleGenerationEn() throws FileNotFoundException, IOException {
		String[] args = new String[] {"-doclet","org.centauron.ant.doclet.Doclet","-d","target/generated-docs/doc2_en","-sourcepath","src/test/resources_java/examples/src","org.centauron.ant.doclet.examples"};
		com.sun.tools.javadoc.Main.execute(args);
		
		File f = new File("target/generated-docs/doc2_en/cover.html");
		assertTrue(f.exists());
		FileReader reader = new FileReader(f);
		String cover = IOUtils.toString(reader);
		reader.close();
		
		assertTrue(cover.contains("manual"));
		
		assertTrue(new File("target/generated-docs/doc2_en/tasks/HelloWorldChildTask.html").exists());
		assertTrue(new File("target/generated-docs/doc2_en/tasks/HelloWorldTask.html").exists());
	}
}

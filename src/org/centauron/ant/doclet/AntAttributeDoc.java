package org.centauron.ant.doclet;

import java.io.OutputStream;
import java.util.logging.Logger;

import com.sun.javadoc.ClassDoc;

public class AntAttributeDoc extends ResourceWriter {

	private AntDoc m_parent;
	private OutputStream m_stream;
	private ClassDoc m_doc;
	private static final Logger logger = Logger.getLogger(AntTaskDoc.class.getName());

	public AntAttributeDoc(AntDoc parent, ClassDoc doc, OutputStream mystream) {
		super(parent);
		m_parent = parent;
		m_stream = mystream;
		m_doc = doc;
	}

	public void writeout() throws Exception {
		this.writeHeader();
		this.writeFooter();

	}
}

package org.centauron.ant.doclet;

import java.io.OutputStream;
import java.util.logging.Logger;

import com.sun.javadoc.ClassDoc;

public class SinglePageDoc extends ResourceWriter {

	protected AntDoc m_parent;
	protected OutputStream m_stream;
	protected ClassDoc m_doc;
	private static final Logger logger = Logger.getLogger(SinglePageDoc.class.getName());

	public SinglePageDoc(AntDoc parent, ClassDoc doc, OutputStream mystream) {
		super(parent);
		m_parent = parent;
		m_stream = mystream;
		m_doc = doc;
	}

	public void writeout() throws Exception {
		
	}
}

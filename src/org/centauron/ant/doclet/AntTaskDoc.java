package org.centauron.ant.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import java.io.OutputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;


public class AntTaskDoc extends SinglePageDoc {


	private static final Logger logger = Logger.getLogger(AntTaskDoc.class.getName());

	public AntTaskDoc(AntDoc parent, ClassDoc doc, OutputStream mystream) {
		super(parent,doc,mystream);
	}

	public void writeout() throws Exception {
		this.writeHeader();
		this.writeDeprecated();
		this.writeDescription();
		this.writeParameters();
		this.writeNestedParameters();
		this.writeExamples();
		this.writeFooter();

	}


	private void writeHeader() throws Exception {
		Object[] arguments = {
			m_parent.getCorrectTaskName(m_doc)
		};
		write(m_stream, "task-header.template", arguments);
	}

	private void writeFooter() throws Exception {
		Object[] arguments = {
			Constants.BRANDING
		};
		write(m_stream, "task-footer.template", arguments);
	}	
	private void writeDeprecated() throws Exception {
		if (m_parent.isDeprecatedTask(m_doc)) {
			Object[] arguments = {
				m_parent.getTagText(m_doc, Constants.TAG_DEPRECATED, true)
			};
			write(m_stream, "task-deprecated.template", arguments);
		}
	}

	private void writeDescription() throws Exception {
		Object[] arguments = {
			m_parent.getDescription(m_doc)
		};
		write(m_stream, "task-description.template", arguments);
	}



	private MethodDoc[] getAllSetParameters(ClassDoc doc) {
		MethodDoc[] m = m_parent.getDocumentableSetMethods(doc);
		if (m_parent.getSuperElements().equalsIgnoreCase("add")) {
			ClassDoc sd = doc.superclass();
			if (sd != null) {
				if (!m_parent.isBaseTask(sd) && m_parent.shouldBeDocumented(sd)) {
					System.out.println(sd.qualifiedTypeName());
					MethodDoc[] m2 = getAllSetParameters(sd);
					m = Utility.concatArray(m, m2);
				}
			}
		}
		return m;

	}

	private void writeParameters() throws Exception {


		MethodDoc[] m = getAllSetParameters(m_doc);
		m = Utility.sortArray(m);
		if (m.length == 0) {
			return;
		}

		write(m_stream, "task-parameters-header.template");
		for (int i = 0; i < m.length; i++) {
			writeSingleParameter(m[i]);
		}
		write(m_stream, "task-parameters-footer.template");
	}

	private void writeSingleParameter(MethodDoc m) throws Exception {
		if (m == null) {
			return;
		}
		String name = m_parent.getNameFromSetter(m);
		String descr = m_parent.getDescription(m);
		if (m_parent.isOptionalMethod(m)) {
			//descr=descr+"<br>"+m_parent.getTagText(m, Constants.TAG_OPTIONAL,true);
			String req = m_parent.getTagText((ProgramElementDoc) m, Constants.TAG_OPTIONAL, true);
			Object[] arguments = {
				name, descr, req
			};
			write(m_stream, "task-parameters-optionalentry.template", arguments);

		} else {
			Object[] arguments = {
				name, descr
			};
			write(m_stream, "task-parameters-entry.template", arguments);
		}
	}

	private void writeExamples() throws Exception {
		int i;
		Vector<String> v = m_parent.getTagTextVector(m_doc, Constants.TAG_EXAMPLE, true);
		Vector<String> vc = m_parent.getTagTextVector(m_doc, Constants.TAG_EXAMPLE_CODE, true);
		Vector<String> vd = m_parent.getTagTextVector(m_doc, Constants.TAG_EXAMPLE_DESCRIPTION, true);
		if (vc.size() != vd.size()) {
			//throw new Exception("Size of ant.example.code(" + vc.size() + ") and ant.example.description(" + vd.size() + ") do not match for Task" + m_doc.qualifiedName() + ".\n Either all or none of the tags must be localized.");
			logger.log(Level.WARNING, "Size of ant.example.code({0}) and ant.example.description({1}) do not match for Task{2}.\n Either all or none of the tags must be localized. Fallback in progress.", new Object[]{vc.size(), vd.size(), m_doc.qualifiedName()});
			/*
			vc = m_parent.getTagTextVector(m_doc, Constants.TAG_EXAMPLE_CODE, false);
			vd = m_parent.getTagTextVector(m_doc, Constants.TAG_EXAMPLE_DESCRIPTION, false);
			
			if (vc.size()!=vd.size()) {
				logger.warning("examples doesn't match description");
			}*/
		}
		if (v.size() == 0 && vc.size() == 0) {
			//NO EXAMPLES
			return;
		}
		write(m_stream, "task-examples-header.template");

		for (i = 0; i < v.size(); i++) {
			writeSingleExample(v.elementAt(i));
		}
		for (i = 0; i < Math.max(vc.size(),vd.size()); i++) {
			if (i<vc.size() && i<vd.size()) 
				writeSingleDetailedExample(vc.elementAt(i), vd.elementAt(i));
			else if (i<vc.size())
				writeSingleDetailedExample(vc.elementAt(i), " <b>missing example description</b> ");
			else
				writeSingleDetailedExample(" <b>missing example</b> ",vd.elementAt(i));			
		}
		write(m_stream, "task-examples-footer.template");
	}

	private void writeSingleExample(String ex) throws Exception {
		Object[] arguments = {
			ex
		};
		write(m_stream, "task-examples-entry.template", arguments);
	}

	private void writeSingleDetailedExample(String x1, String x2) throws Exception {
		Object[] arguments = {
			StringEscapeUtils.escapeXml(x1),
			x2
		};
		write(m_stream, "task-examples-detailedentry.template", arguments);
	}

	private Vector<String[]> getAllNestedParameters(ClassDoc doc) throws Exception {
		Vector<String[]> v = m_parent.getSpecialNestedParameters(doc);
		Vector<String[]> vg = m_parent.getGeneralNestedParameters(doc);
		v.addAll(vg);
		if (doc.superclass() != null) {
			Vector<String[]> vs = this.getAllNestedParameters(doc.superclass());
			v.addAll(vs);
		}
		/*
		 * if (m_parent.getSuperElements().equalsIgnoreCase("add")) { ClassDoc
		 * sd=doc.superclass(); if (sd!=null) { if (!m_parent.isBaseTask(sd) &&
		 * m_parent.shouldBeDocumented(sd)) {
		 * System.out.println(sd.qualifiedTypeName()); Vector<String[]>
		 * v2=this.getAllNestedParameters(sd); v.addAll(v2); } } }
		 */
		return v;
	}

	private void writeNestedParameters() throws Exception {
		Vector<String[]> v = getAllNestedParameters(m_doc);
		if (v.size() == 0) {
			return;
		}
		write(m_stream, "task-nestedparameters-header.template");
		for (int i = 0; i < v.size(); i++) {
			String[] sar = v.elementAt(i);
			//System.out.println("NESTEDPARAMETER:"+sar[0]);
			//System.out.println("NESTEDPARAMETER-DESCRIPTION:"+sar[1]);
			writeSingleNestedParameter(sar[0], sar[1]);
		}
		write(m_stream, "task-nestedparameters-footer.template");

	}

	private void writeSingleNestedParameter(String x1, String x2) throws Exception {
		Object[] arguments = {
			x1, x2
		};
		write(m_stream, "task-nestedparameters-entry.template", arguments);
	}
}

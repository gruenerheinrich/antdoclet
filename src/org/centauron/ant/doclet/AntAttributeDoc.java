package org.centauron.ant.doclet;

import java.io.OutputStream;
import java.util.Vector;
import java.util.logging.Logger;

import org.centauron.utils.RunTimeUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;

public class AntAttributeDoc extends SinglePageDoc {


	private static final Logger logger = Logger.getLogger(AntTaskDoc.class.getName());

	public AntAttributeDoc(AntDoc parent, ClassDoc doc, OutputStream mystream) {
		super(parent,doc,mystream);
	}
	public void writeout() throws Exception {
		this.writeHeader();
		this.writeDescription();
		this.writeAttributes();
		this.writeFooter();

	}
	private void writeHeader() throws Exception {
		Object[] arguments = {
			m_parent.getCorrectTaskName(m_doc)
		};
		write(m_stream, "task-header.template", arguments);
	}
	private void writeDescription() throws Exception {
		Object[] arguments = {
			m_parent.getDescription(m_doc)
		};
		write(m_stream, "task-description.template", arguments);
	}	
	
	private boolean hasCommentText(FieldDoc f) {
		if (f.commentText()==null) return true;
		if (f.commentText().length()==0) return true;
		return true;
	}
	
	private FieldDoc[] getDocumentedVariables(ClassDoc doc) {
		FieldDoc[] dd=doc.fields();
		Vector<FieldDoc> v=new Vector();
		for (FieldDoc fd:dd) {
			if (hasCommentText(fd)) {
				v.add(fd);
			}
		}
		return Utility.toArray(v);
	}
	
	private void writeAttributes() throws Exception {


		FieldDoc[] ff=getDocumentedVariables(m_doc);

		write(m_stream, "attribute-parameters-header.template");
		for (FieldDoc f:ff) {
			Class cl=RunTimeUtils.loadClass(this.getClass().getClassLoader(), m_doc);
			String[] vals=null;
			if (f.isStatic()) {
				vals=(String[])RunTimeUtils.loadStaticFieldInstance(cl, f.name());
			}
			Vector<String> comms=m_parent.getTagTextVector(f,Constants.TAG_DESCRIPTION , true);
			String allcomms=Utility.joinStrings(comms,"");
			comms=Utility.split(allcomms,System.getProperty("line.separator"));
			System.out.println("Name:"+f.name());
			int mm=comms.size();
			if (vals!=null) {
				if (vals.length!=comms.size()) {
					System.err.println("ACHTUNG: Unterschiedliche ParameterLängen");
				}
				mm=Math.max(vals.length,comms.size());
			}
			for (int i=0;i<mm;i++) {
				String value=null;
				if (vals!=null) {
					if (vals.length>i) {
						value=vals[i];
					}
				}
				System.out.println("VALUE:"+value);				
				String descr=getCommentField(comms,i);			
				Vector<String> descrfield=Utility.split(descr,"#");
				if (descrfield.size()==2) {
					value=descrfield.elementAt(0);
					descr=descrfield.elementAt(1);
				}
				Object[] arguments = {
					value,descr
				};
				write(m_stream, "attribute-parameters-entry.template", arguments);
			}
		}
		write(m_stream, "attribute-parameters-footer.template");
	}

	private String getCommentField(Vector<String> comms,int idx) {
		if (idx>=comms.size()) {
			return "";
		}
		return comms.elementAt(idx);
	}
	private void writeFooter() throws Exception {
		Object[] arguments = {
			Constants.BRANDING
		};
		write(m_stream, "task-footer.template", arguments);
	}
}

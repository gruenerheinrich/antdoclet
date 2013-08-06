package org.centauron.ant.doclet;

import com.sun.javadoc.*;
import com.sun.tools.doclets.Taglet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Antdoc doclet
 *
 * @author centauron
 * @TODO Templates should be outside.
 */
public class AntDoc {

	private static final Logger logger = Logger.getLogger(AntDoc.class.getName());
	private RootDoc m_Root = null;
	private String m_Output = ".";
	private String m_Overview = null;
	private String m_sourcepath = null;
	private String m_Title = "Ant tasks";
	private String m_resourcedir=null;
	private String m_superelements = "ignore";
	private Vector<String[]> m_groups = new Vector();
	private Hashtable<String, String> m_taskdefoptionsentries = new Hashtable();
	private Vector<String> m_taskdefs = new Vector();
	private Vector<Taglet> m_taglet = new Vector();
	private StaticResourceDoc m_Statics;
	private Locale m_locale;
	private String[] m_baseclassNames = {"org.apache.tools.ant.Task", "org.apache.tools.ant.taskdefs.ConditionTask"};
	private String[] m_baseAttributeNames={"org.apache.tools.ant.types.EnumeratedAttribute"};
	public AntDoc(RootDoc root) {
		m_Root = root;
		m_Statics = new StaticResourceDoc(this);
		readOptions(root.options());
	}

    public Locale getLocale() {
		return m_locale;
	}

	public String getOutput() {
		return m_Output;
	}
	
	public String getUserResourcePath() {
		return m_resourcedir;
	}
	
    private void setUserResourcePath(String string) {
    	File f=new File(string);
    	m_resourcedir=f.getAbsolutePath()+"/";
	}	

	public void write() throws Exception {
		File dir = new File(m_Output);
		dir.mkdirs();

		//CLEAN IT UP
		Utility.deleteAllFilesInDir(dir);
		//BUILD DIR STRUCTURE
		File subdir = new File(dir, "tasks");
		subdir.mkdir();
		Object[] args = {m_Title};
		m_Statics.writeFile("index.html", "index.html", args);
		m_Statics.writeFile("style.css", "stylesheets/style.css");
		m_Statics.writeFile("logo_gross.png", "images/logo_gross.png");
		m_Statics.writeFile("cover.html");
		writeTOC();
		writeNavigationForGroup("All Tasks", null, "alltasklist");
		System.out.println("GROUP SIZE" + Integer.toString(m_groups.size()));
		for (int i = 0; i < m_groups.size(); i++) {
			String[] elm = m_groups.elementAt(i);
			writeNavigationForGroup(elm[0], elm[1], elm[2]);
		}
		return;

	}

	public String getSuperElements() {
		return m_superelements;
	}

	private String getNameFromTaskDefOption(String cname) {
		return m_taskdefoptionsentries.get(cname);

	}

	public boolean isDeprecatedTask(ClassDoc d) {
		Tag[] tags;
		tags = d.tags(Constants.TAG_DEPRECATED);
		if (tags.length > 0) {
			return true;
		}
		return false;

	}

	public String getDescription(MethodDoc d) {
		String s = "";
		s = this.getTagText(d, Constants.TAG_DESCRIPTION, true);
		if (s.length() == 0) {
			if (m_locale != null) {
				s = getTagText(d, "@" + m_locale.getLanguage() + "." + m_locale.getCountry(), false);
				if (s.length() == 0) {
					s = getTagText(d, "@" + m_locale.getLanguage(), false);
				}
			}
		}
		if (s.length() == 0) {
			s = d.commentText();
		}
		return s;
	}

	public String getDescription(ClassDoc d) {
		String s = "";
		s = this.getTagText(d, Constants.TAG_DESCRIPTION, true);
		if (s.length() == 0) {
			if (m_locale != null) {
				s = getTagText(d, "@" + m_locale.getLanguage() + "." + m_locale.getCountry(), false);
				if (s.length() == 0) {
					s = getTagText(d, "@" + m_locale.getLanguage(), false);
				}
			}
		}
		if (s.length() == 0) {
			s = this.resolveInlineTags(d);
		}
		return s;
	}

	public String getTagText(ProgramElementDoc d, String tagName, boolean shouldaddlocale) {
		String s = this.getTagText(d.tags(), tagName, shouldaddlocale);
		return s;
	}

	public String getTagText(Tag[] tags, String tagName, boolean shouldaddlocale) {
		String s = "";
		Vector<String> v = getTagTextVector(tags, tagName, shouldaddlocale);
		for (int i = 0; i < v.size(); i++) {
			s = s + v.elementAt(i) + "<br>";
		}
		if (s.length() > 0) {
			s = s.substring(0, s.length() - 4);
		}
		return s;
	}

	public Vector<String> getTagTextVector(ClassDoc d, String tagName, boolean shouldaddlocale) {
		return this.getTagTextVector(d.tags(), tagName, shouldaddlocale);
	}

	public Vector<String> getTagTextVector(MethodDoc d, String tagName, boolean shouldaddlocale) {
		return this.getTagTextVector(d.tags(), tagName, shouldaddlocale);
	}
	public Vector<String> getTagTextVector(FieldDoc d, String tagName, boolean shouldaddlocale) {
		return this.getTagTextVector(d.tags(), tagName, shouldaddlocale);
	}
	
	public Vector<String> getTagTextVector(Tag[] tags, String tagName, boolean shouldaddlocale) {
		Vector<String> v = new Vector();
		if (m_locale != null) {
			//EXTRACT ANY LOCALIZED TAGS
			if (shouldaddlocale) {
				v = getTagTextVector(tags, tagName + "." + m_locale.getLanguage() + "." + m_locale.getCountry(), false);
				if (v.size() == 0) {
					v = getTagTextVector(tags, tagName + "." + m_locale.getLanguage(), false);
				}
			}
		}
		if (v.size() > 0) {
			return v;
		}
		for (int i = 0; i < tags.length; i++) {
			if (tags[i].name().equalsIgnoreCase(tagName)) {
				String s = this.resolveInlineTags(tags[i]);
				v.addElement(s);
			}
		}
		return v;
	}

	public String getCorrectTaskName(ClassDoc d) {
		String taskname = null;
		String classname;
		String defname;
		Tag[] tags;

		classname = d.qualifiedName();

		tags = d.tags(Constants.TAG_TASKNAME);
		if (tags.length > 0) {
			taskname = tags[0].text();
		}
		defname = this.getNameFromTaskDefOption(classname);
		if (defname != null) {
			if (taskname != null) {
				if (!defname.equalsIgnoreCase(taskname)) {
					System.out.println("Taskname from Annotation ant.taskname and Name in Task Definition File differ for " + taskname + ".");
				}
			} else {
				taskname = defname;
			}

		}
		if (taskname == null) {
			taskname = d.name();
		}

		return taskname;
	}

	public boolean shouldBeDocumented(ClassDoc d) {
		if (!(this.isTaskClass(d) || this.isAttributeClass(d))) {
			return false;
		}
		Tag[] tags = d.tags(Constants.TAG_NODOC);
		if (tags.length == 0) {
			return true;
		}
		return false;

	}

	private int rateThisPackage(ClassDoc d, String pname) {
		String nameofclass = d.qualifiedName();
		if (nameofclass.indexOf(pname) == 0) {
			return pname.length();
		}
		return -1;
	}

	private boolean shouldBeInThisPackage(ClassDoc d, String pname) {
		if (pname == null) {
			return true;
		}
		int r = this.rateThisPackage(d, pname);
		if (r > 0) {
			for (int i = 0; i < m_groups.size(); i++) {
				String[] o = m_groups.elementAt(i);
				if (!o[1].equals(pname)) {
					if (r < this.rateThisPackage(d, o[1])) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	private void writeTOC() throws Exception {
		TaskTocResourceWriter allframe = new TaskTocResourceWriter(this, new FileOutputStream(m_Output + File.separatorChar + "toc.html"));
		allframe.writeHeader();
		allframe.writeGroups(m_groups);
		allframe.writeFooter();

	}

	private void writeNavigationForGroup(String groupname, String packagename, String fname) throws Exception {
		ClassDoc[] classes = m_Root.classes();
		TaskTocResourceWriter allframe = new TaskTocResourceWriter(this, new FileOutputStream(m_Output + File.separatorChar + fname + ".html"));
		allframe.writeHeader();
		allframe.writeGroups(m_groups);
		allframe.writeHeading(groupname);
		// Sort the Tasks
		Hashtable<String, ClassDoc> cmap = new Hashtable();
		for (int i = 0; i < classes.length; i++) {
			cmap.put(getCorrectTaskName(classes[i]), classes[i]);
		}

		TreeSet cset = new TreeSet(cmap.keySet());
		Iterator citer = cset.iterator();
		while (citer.hasNext()) {
			ClassDoc classdoc = cmap.get(citer.next());
			if (this.shouldBeDocumented(classdoc)) {
				String taskfilename = "tasks" + File.separatorChar + this.getCorrectTaskName(classdoc) + ".html";
				if (this.shouldBeInThisPackage(classdoc, packagename)) {
					allframe.writeTask(taskfilename, this.getCorrectTaskName(classdoc), this.isDeprecatedTask(classdoc));
				}
				//DOCUMENT THE TASK
				if (packagename == null) {
					final FileOutputStream fos = new FileOutputStream(m_Output + File.separatorChar + taskfilename);
					//writeOutTagNames(classdoc);
					if (this.isAttributeClass(classdoc)) {
						AntAttributeDoc dd=new AntAttributeDoc(this,classdoc,fos);
						dd.writeout();
					}
					if (this.isTaskClass(classdoc)) {
						AntTaskDoc dd = new AntTaskDoc(this, classdoc, fos);
						dd.writeout();
					}
					fos.close();
				}
			}
		}

		allframe.writeFooter();
	}

	private void readTaskDefEntries() {

		try {
			for (int x = 0; x < m_taskdefs.size(); x++) {
				String rs = m_taskdefs.elementAt(x);
				BufferedReader br = new BufferedReader(new FileReader(m_sourcepath + File.separatorChar + rs));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					int i = strLine.indexOf("=");
					if (i > 0) {
						String cn = strLine.substring(i + 1, strLine.length());
						String dn = strLine.substring(0, i);
						//System.out.println(cn+" --- "+dn);
						m_taskdefoptionsentries.put(cn, dn);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	private String getGroupFileNameFromName(String s) {
		s = s.toLowerCase();
		s = s.replaceAll(" ", "");
		return s + "list";
	}

	private void sortGroups() {
		TreeSet cset = new TreeSet();
		Hashtable<String, String[]> hh = new Hashtable();
		Vector<String[]> nn = new Vector();
		for (int i = 0; i < m_groups.size(); i++) {
			cset.add(m_groups.elementAt(i)[0]);
			hh.put(m_groups.elementAt(i)[0], m_groups.elementAt(i));
		}
		Iterator it = cset.iterator();
		while (it.hasNext()) {
			nn.add(hh.get(it.next()));
		}
		m_groups = nn;
	}

	public void writeOutTagNames(ClassDoc d) {
		System.out.println("TAGS FOR " + d.qualifiedName());
		Tag[] tt = d.tags();
		for (int i = 0; i < tt.length; i++) {
			System.out.println(tt[i].name() + " = " + tt[i].text());
		}
	}

	public void writeOutTagNames(MethodDoc d) {
		System.out.println("TAGS FOR " + d.qualifiedName());
		Tag[] tt = d.tags();
		for (int i = 0; i < tt.length; i++) {
			System.out.println(tt[i].name() + " = " + tt[i].text());
		}
	}

	private boolean isSetMethod(MethodDoc m) {
		if (m.returnType().qualifiedTypeName().equals("void")) {
			if (m.name().startsWith("set")) {
				if (m.isPublic()) {
					return true;
				}
			}
		}
		return false;
	}

	public MethodDoc[] getDocumentableSetMethods(ClassDoc d) {
		MethodDoc[] m = d.methods();
		Vector<MethodDoc> v = new Vector();
		for (int i = 0; i < m.length; i++) {
			if (isSetMethod(m[i])) {
				if (this.isDocumentableMethod(m[i])) {
					v.add(m[i]);
				}
			}
		}
		MethodDoc[] arr = new MethodDoc[v.size()];
		v.toArray(arr);
		return arr;
	}

	public String getNameFromSetter(MethodDoc m) {
		String s = m.name();
		s = s.substring(3);
		s = s.toLowerCase();
		return s;
	}

	public boolean isOptionalMethod(MethodDoc m) {
		return (this.getTagTextVector(m, Constants.TAG_OPTIONAL, true).size() != 0);
	}

	public boolean isDocumentableMethod(MethodDoc m) {
		return (this.getTagTextVector(m, Constants.TAG_NODOC, false).size() == 0);
	}

	public Vector<String[]> getSpecialNestedParameters(ClassDoc mDoc) {
		Vector<String[]> v = new Vector();
		MethodDoc[] meth = mDoc.methods();
		// VIA ADD METHOD
		for (int i = 0; i < meth.length; i++) {
			String name = meth[i].name();
			if (!name.equalsIgnoreCase("add") && !name.equalsIgnoreCase("addtask")) {
				if (isTaskAddingFunction(meth[i])) {
					String s[] = new String[2];
					s[0] = name.substring(3);
					s[1] = this.getDescription(meth[i]);
					v.add(s);
				}
			}
		}
		return v;
	}

	private boolean isTaskAddingFunction(MethodDoc m) {
		String name = m.name();
		if (!name.startsWith("add")) {
			return false;
		}
		Parameter[] pt = m.parameters();
		if (pt == null) {
			return false;
		}
		ClassDoc firstParam = pt[0].type().asClassDoc();
		if (this.isTaskClass(firstParam)) {
			return true;
		}
		return false;

	}

	public Vector<String[]> getGeneralNestedParameters(ClassDoc mDoc) throws Exception {
		Vector<String[]> v = new Vector();
		MethodDoc[] meth = mDoc.methods();
		for (int i = 0; i < meth.length; i++) {
			String name = meth[i].name();
			if (name.equalsIgnoreCase("add") || name.equalsIgnoreCase("addtask")) {
				if (isTaskAddingFunction(meth[i])) {
					//GET ENTRIES FROM NOTATION
					Vector<String> vn = this.getTagTextVector(meth[i], Constants.TAG_NESTEDPARAMETER_NAME, true);
					Vector<String> vd = this.getTagTextVector(meth[i], Constants.TAG_NESTEDPARAMETER_DESCRIPTION, true);
					if (vn.size() != vd.size()) {
						throw new Exception("Nested Parameter Names and Description differ in " + mDoc.name() + ". Correct localized?");
					}
					for (int x = 0; x < vn.size(); x++) {
						String s[] = new String[2];
						s[0] = vn.elementAt(x);
						s[1] = vd.elementAt(x);
						v.add(s);
					}
				}
			}
		}
		return v;
	}

	private String resolveLinkTarget(String dname) {
		if (dname.startsWith("http://")) {
			return dname;
		}
		return dname + ".html";
	}

	public String resolveInlineTag(Tag t) {
		//System.out.println("INLINE NAME:"+t.name()+"!");
		//System.out.println("INLINE TEXT:"+t.text());
		if (t.name().equalsIgnoreCase(Constants.STANDARDTAG_CODE)) {

			if (Utility.isMultiLine(t.text())) {
				return "<pre style=\"background:#FFFFFF\">" + StringEscapeUtils.escapeXml(t.text()) + "</pre>";
			}

			return "<code>" + StringEscapeUtils.escapeXml(t.text()) + "</code>";
		}
		if (t.name().equalsIgnoreCase(Constants.STANDARDTAG_LITERAL)) {
			return StringEscapeUtils.escapeXml(t.text());
		}
		if (t.name().equalsIgnoreCase(Constants.STANDARDTAG_LINK)) {
			String dest = Utility.getStringPart(t.text(), 0);
			//System.out.println("DEST IS:"+dest);
			String cap = Utility.getStringPart(t.text(), 1);
			if (cap == null) {
				cap = dest;
			}
			//TODO CHECK IS ANY EXTERNAL LINKAGE OPTION
			dest = resolveLinkTarget(dest);
			String targ = "_self";
			/*
			 * if (dest.startsWith("http://")) { targ="info"; }
			 */
			return "<a target=\"" + targ + "\" href=\"" + dest + "\"><code>" + cap + "</code></a>";
		}

		Taglet tl = this.getTagletForTag(t);
		if (tl != null) {
			//SHOULD BE LOCALIZABLE
			System.out.println("USE TAGLET....");
			return tl.toString(t);
		}


		return "";
	}

	public Taglet getTagletForTag(Tag t) {
		for (int i = 0; i < m_taglet.size(); i++) {
			Taglet tl = m_taglet.elementAt(i);
			if (tl.getName().equalsIgnoreCase(t.name())) {
				return tl;
			}
		}
		return null;
	}

	public String resolveInlineTags(Tag[] tl) {
		String s = "";
		if (tl == null) {
			return "";
		}
		for (int i = 0; i < tl.length; i++) {
			Tag t = tl[i];
			if (t.kind().equalsIgnoreCase("text")) {
				s = s + t.text();
			} else {
				s = s + this.resolveInlineTag(t);
			}
		}
		return s;
	}

	public String resolveInlineTags(Tag t) {
		//INLINE TAGS IN A TAG
		return this.resolveInlineTags(t.inlineTags());
	}

	public String resolveInlineTags(ProgramElementDoc d) {
		//INLINE TAGS IN A ELEMENT
		return this.resolveInlineTags(d.inlineTags());
	}

	private void readOptions(String opts[][]) {
		String[] sw = null;

		for (int o = 0; o < opts.length; o++) {
			if (opts[o][0].equals("-d")) {
				m_Output = opts[o][1];
			}
			if (opts[o][0].equals("-title")) {
				m_Title = opts[o][1];
			}
			if (opts[o][0].equals("-resourcedir")) {
				setUserResourcePath(opts[o][1]);
			}
			if (opts[o][0].equals("-doctitle")) {
				m_Title = opts[o][1];
			}
			if (opts[o][0].equals("-sourcepath")) {
				m_sourcepath = opts[o][1];
			}
			if (opts[o][0].equals("-locale")) {
				m_locale = LocaleUtils.toLocale(opts[o][1]);
			}
			if (opts[o][0].equals("-windowtitle")) {
				m_Title = opts[o][1];
			}
			if (opts[o][0].equals("-overview")) {
				m_Overview = opts[o][1];
			}
			if (opts[o][0].equals("-group")) {
				String[] ent = new String[3];
				ent[0] = opts[o][1];
				ent[1] = opts[o][2];
				ent[2] = getGroupFileNameFromName(ent[0]);
				m_groups.add(ent);
			}
			if (opts[o][0].equals("-taskdef")) {
				//READ RESSOURCE IN THE END, BECAUSE READING DEPENDS SOURCE PATH
				m_taskdefs.add(opts[o][1]);
			}
			if (opts[o][0].equals("-taglet")) {
				//READ RESSOURCE IN THE END, BECAUSE READING DEPENDS SOURCE PATH
				initTaglet(opts[o][1], null);
			}
			if (opts[o][0].equals("-superelements")) {
				m_superelements = opts[o][1];
			}

		}

		sortGroups();
		readTaskDefEntries();
	}

	;	
    


	private void initTaglet(String tagletname, String tagletpath) {
		try {
			Taglet t = null;
			if (tagletpath == null) {
				Class cl = Class.forName(tagletname);
				Object object = cl.newInstance();
				t = (Taglet) object;
			}
			m_taglet.add(t);
		} catch (Exception e) {
			System.out.println("ERROR USING TAGLET: " + tagletname);
		}
	}

	public boolean isBaseTask(ClassDoc sd) {
		for (int i = 0; i < m_baseclassNames.length; i++) {
			if (sd.qualifiedName().equalsIgnoreCase(m_baseclassNames[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBaseAttribute(ClassDoc sd) {
		for (int i = 0; i < m_baseAttributeNames.length; i++) {
			if (sd.qualifiedName().equalsIgnoreCase(m_baseAttributeNames[i])) {
				return true;
			}
		}
		return false;
	}	

	
	public boolean isAttributeClass(ClassDoc d) {
		if (isBaseAttribute(d)) {
			return true;
		}
		for (int i = 0; i < m_baseAttributeNames.length; i++) {
			if (m_Root.classNamed(m_baseAttributeNames[i]) != null) {
				if (d.subclassOf(m_Root.classNamed(m_baseAttributeNames[i]))) {
					return true;
				}
			} else {
				logger.log(Level.WARNING, "{0} not found in classpath", m_baseAttributeNames[i]);
			}
		}		
		return false;
	}
	
	public boolean isTaskClass(ClassDoc d) {
		if (isBaseTask(d)) {
			return true;
		}
		for (int i = 0; i < m_baseclassNames.length; i++) {
			if (m_Root.classNamed(m_baseclassNames[i]) != null) {
				if (d.subclassOf(m_Root.classNamed(m_baseclassNames[i]))) {
					return true;
				}
			} else {
				logger.log(Level.WARNING, "{0} not found in classpath", m_baseclassNames[i]);
			}
		}
		return false;

	}

	public void analyzeClassDoc(ClassDoc doc) {
		System.out.println(doc.name());
		System.out.println("   Methods:"+doc.methods().length);	
		System.out.println("   Fields:"+doc.fields().length);
		System.out.println("   Constructors:"+doc.constructors().length);
		System.out.println("   Enum Const:"+doc.enumConstants().length);
		System.out.println("   Serial Fields:"+doc.serializableFields().length);
		System.out.println("   Type params:"+doc.typeParameters().length);
	}


}
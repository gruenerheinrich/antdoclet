package org.centauron.ant.doclet;

import com.sun.javadoc.MethodDoc;
import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;

public class Utility {

	public final static Logger logger = Logger.getLogger(Utility.class.getName());

	public static void deleteAllFilesInDir(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				Utility.deleteAllFilesInDir(file);
			}
			if (!file.delete()) {
				logger.log(Level.WARNING, "Failed to delete {0}", file);
			}
		}
	}

	public static boolean isMultiLine(String s) {
		return s.indexOf("\n") != -1;
	}

	public static String escapeXMLString(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	public static String lowerFirstCharacter(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	public static String getStringPart(String s, int idx) {
		StringTokenizer tok = new StringTokenizer(s, " ");
		if (tok.countTokens() <= idx) {
			return null;
		}
		for (int i = 0; i < idx; i++) {
			tok.nextToken();
		}
		return tok.nextToken();
	}

	public static MethodDoc[] concatArray(MethodDoc[] m, MethodDoc[] m2) {
		MethodDoc a[] = new MethodDoc[m.length + m2.length];
		for (int i = 0; i < m.length; i++) {
			a[i] = m[i];
		}
		for (int i = 0; i < m2.length; i++) {
			a[i + m.length] = m2[i];
		}
		return a;
	}

	public static MethodDoc[] sortArray(MethodDoc[] m) {
		MethodDoc[] a = new MethodDoc[m.length];
		TreeMap<String, MethodDoc> t = new TreeMap();
		for (int i = 0; i < m.length; i++) {
			t.put(m[i].name(), m[i]);
		}
		int i = 0;
		Iterator<MethodDoc> it = t.values().iterator();
		while (it.hasNext()) {
			a[i] = it.next();
			++i;
		}
		return a;
	}
}

package org.centauron.ant.doclet;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;

public class Utility {

	public final static Logger logger = Logger.getLogger(Utility.class.getName());

	/**
	 * Removes all files in a directory.
	 *
	 * @param dir
	 */
	public static void deleteAllFilesInDir(File dir) {
		if (dir.exists()) {
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
	}

	/**
	 * Tests whether its a multiline string or not.
	 *
	 * @param s
	 * @return
	 */
	public static boolean isMultiLine(String s) {
		return s.indexOf("\n") != -1;
	}

	/**
	 * Lowers the first character of the string.
	 *
	 * @param s
	 * @return
	 */
	public static String lowerFirstCharacter(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * Delivers the nth part of a string. The index is 0 based. Indexes lower
	 * than 0 returning the index 0.
	 *
	 * @param s
	 * @param idx
	 * @return
	 */
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

	/**
	 * Concatenate two arrays of MethodDoc arrays.
	 *
	 * @param m
	 * @param m2
	 * @return
	 */
	public static MethodDoc[] concatArray(MethodDoc[] m, MethodDoc[] m2) {
		MethodDoc a[] = new MethodDoc[m.length + m2.length];
		System.arraycopy(m, 0, a, 0, m.length);
		System.arraycopy(m2, 0, a, m.length, m2.length);
		return a;
	}

	/**
	 * Sort an array of MethodDoc s regarding the name attribute.
	 *
	 * @param m
	 * @return
	 */
	public static MethodDoc[] sortArray(MethodDoc[] m) {
		Arrays.sort(m, new Comparator<MethodDoc>() {

			@Override
			public int compare(MethodDoc o1, MethodDoc o2) {
				return o1.name().compareTo(o2.name());
			}
		});
		return m;
	}
	
	public static FieldDoc[] toArray(Vector<FieldDoc> v) {
		FieldDoc[] ff=new FieldDoc[v.size()];
		int i=0;
		for (FieldDoc f:v) {
			ff[i]=f;
			++i;
		}
		return ff;
	}
	
	public static String joinStrings(Vector<String> v,String delim) {
		StringBuffer buf=new StringBuffer();
		for (String s:v) {
			if (buf.length()!=0) buf.append(delim);
			buf.append(s);
		}
		return buf.toString();
	}

	public static Vector<String> split(String allcomms, String delim) {
		Vector<String> vec=new Vector();
		StringTokenizer stok=new StringTokenizer(allcomms,delim);
		while (stok.hasMoreTokens()) {
			vec.addElement(stok.nextToken());
		}
		return vec;
	}
}

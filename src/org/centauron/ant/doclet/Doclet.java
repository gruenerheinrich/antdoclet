package org.centauron.ant.doclet;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

/**
 * Antdoc doclet
 *
 * Copyright: Copyright (c) 2002, Toni Thomsson, All rights reserved.
 *
 * @author centauron
 * @version 0.5
 */
public class Doclet {

	private static final String USAGE = "Usage: javadoc -doclet org.centauron.ant.doclet.Doclet [options] [standard options]\n"
			+ "-?          Help, this text\n"
			+ "-d          Output directory, default: .\n"
			+ "-title      Document title, default: \"Ant tasks\"\n";

	public static void writeoutOptions(RootDoc root) {
		String[][] o = root.options();

		for (int i = 0; i < o.length; i++) {
			String l[] = o[i];
			for (int x = 0; x < l.length; x++) {
				System.out.println(l[x]);
			}
		}
	}

	public static boolean start(RootDoc root) {
		writeoutOptions(root);
		boolean result = true;
		try {
			AntDoc doc = new AntDoc(root);
			doc.write();
		} catch (Throwable e) {
			result = false;
			e.printStackTrace();
			System.err.println(e);
		}

		return result;
	}

	public static int optionLength(String option) {
		if (option.equals("-?")) {
			return 1;
		}
		if (option.equals("-d")) {
			return 2;
		}
		if (option.equals("-title")) {
			return 2;
		}
		if (option.equals("-doctitle")) {
			return 2;
		}
		if (option.equals("-windowtitle")) {
			return 2;
		}
		if (option.equals("-group")) {
			return 3;
		}
		if (option.equals("-taskdef")) {
			return 2;
		}
		if (option.equals("-sourcepath")) {
			return 2;
		}
		if (option.equals("-taglet")) {
			return 2;
		}
		if (option.equals("-superelements")) {
			return 2;
		}

		return 0;
	}

	public static boolean validOptions(String opts[][], DocErrorReporter reporter) {
		boolean foundOption = true;

		String[] sw = null;

		for (int o = 0; o < opts.length; o++) {
			if (opts[o][0].equals("-?")) {
				foundOption = false;
			}
		}

		if (!foundOption) {
			reporter.printError(USAGE);
		}

		return foundOption;
	}
}

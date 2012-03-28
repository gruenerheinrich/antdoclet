package org.centauron.ant.doclet;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Antdoc doclet
 *
 * Copyright: Copyright (c) 2002, Toni Thomsson, All rights reserved.
 *
 * @author centauron
 */
public class StaticResourceDoc extends ResourceWriter {

	public StaticResourceDoc(AntDoc pa) {
		super(pa);
	}

	public void writeFile(String fname, String newfilename, Object[] args) throws Exception {
		//CHECK IF DIR FOR newfileexists
		String nf = m_parent.getOutput() + File.separatorChar + newfilename;
		File f = new File(nf);
		f.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(nf);
		write(out, fname, args);
	}

	;
	public void writeFile(String fname, String newfilename) throws Exception {
		writeFile(fname, newfilename, null);
	}

	;
	
	public void writeFile(String fname) throws Exception {
		writeFile(fname, fname);
	}
;
	
}
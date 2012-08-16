package org.centauron.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Utility {
	
	public static boolean copyFile(File out,File in,boolean allowoverwrite) throws Exception {
		FileInputStream fin=new FileInputStream(in);
		return Utility.copyContent(out, fin, allowoverwrite);
	}
	public static boolean copyContent(File out,InputStream in,boolean allowoverwrite) throws Exception {
		if (!allowoverwrite && out.exists()) return false;

			out.getParentFile().mkdirs();
			int b;
			OutputStream os=new FileOutputStream(out);
			while ((b=in.read())!=-1) {
				os.write(b);
			}
			os.flush();
			os.close();

		return true;
	}
}

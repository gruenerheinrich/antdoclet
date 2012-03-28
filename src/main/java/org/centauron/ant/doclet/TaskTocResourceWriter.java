package org.centauron.ant.doclet;

import com.sun.javadoc.*;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.text.*;
import java.util.Vector;

/**
 * Antdoc doclet
 *
 * Copyright: Copyright (c) 2002, Toni Thomsson, All rights reserved.
 * @author tonjac
 * @version 0.5
 */
public class TaskTocResourceWriter extends ResourceWriter
{
    private OutputStream m_Output = null;

    public TaskTocResourceWriter(AntDoc parent, OutputStream out )
    {
    	super(parent);
        m_Output = out;
    };

    public void writeHeader() throws Exception
    {
        write( m_Output, "toc-header.template" );
    };
    
    
    
    public void writeFooter() throws Exception
    {
        write( m_Output, "toc-footer.template" );
    };

    public void writeTask( String filename, String taskname,boolean deprecated ) throws Exception
    {
        Object[] arguments = {
            filename,
            taskname
         };
        if (deprecated) {
        	System.out.println("DO DEPRECATED");
        	write( m_Output, "toc-deprecatedentry.template", arguments );
        } else {
        	write( m_Output, "toc-entry.template", arguments );
        }
        }

    private void writeGroup(String gname,String fname) throws Exception {
        Object[] arguments = {
                fname + ".html",
                gname
             };

            write( m_Output, "toc-groupentry.template", arguments );   	
    }
	public void writeGroups(Vector<String[]> mGroups) throws Exception {
		writeGroup("All Tasks","alltasklist");		
		for (int i=0;i<mGroups.size();i++) {
			String gname=mGroups.elementAt(i)[0];
			String gfname=mGroups.elementAt(i)[2];
			writeGroup(gname,gfname);
		}
	}

	public void writeHeading(String groupname) throws Exception {
        Object[] arguments = {
                groupname
             };

            write( m_Output, "toc-groupheading.template", arguments );   	
		
	};
}
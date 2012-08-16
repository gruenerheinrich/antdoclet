package org.centauron.ant.doclet;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.text.*;

/**
 * Antdoc doclet
 *
 * Copyright: Copyright (c) 2002, Toni Thomsson, All rights reserved.
 * @author tonjac
 * @version 0.5
 */
public class ResourceWriter
{
	public AntDoc m_parent;
	private final String m_resourcepath="/org/centauron/ant/doclet/resources/";
	public ResourceWriter(AntDoc d) {
		m_parent=d;
	}
    protected ResourceWriter()
    {

    };
    
    
    public void write( OutputStream out, String resource, Object[] arguments) throws Exception 
    {
    	if (arguments==null) {
    		this.write(out,resource);
    		return;
    	}
    	resource=getPossiblyLocalizedResource(resource);
        StringBuffer template = new StringBuffer();
        InputStream index = getClass().getResourceAsStream( resource );
        if (index==null) {
        	System.out.println("RESOURCE IS NULL");
        }
        byte[] buff = new byte[10000];
        int size = 0;

        while( (size = index.read( buff )) > 0 )
            template.append( new String( buff, 0, size ) );        
        String format = MessageFormat.format( template.toString(), arguments );
        out.write( format.getBytes() );
    	
    }

    protected void write( OutputStream out, String resource ) throws Exception
    {
    	resource=getPossiblyLocalizedResource(resource);
        InputStream index = getClass().getResourceAsStream( resource );
        byte[] buff = new byte[10000];
        int size = 0;

        while( (size = index.read( buff )) > 0 )
            out.write( buff, 0, size );
    };
    
    public String getPossiblyLocalizedResource(String t) throws Exception {
    	String resource=m_resourcepath+t;
    	if (m_parent!=null) {
	    	if (m_parent.getLocale()!=null) {
	    		if (m_parent.getLocale().getCountry().length()>0) {
		    		resource=m_resourcepath+m_parent.getLocale().getLanguage()+"/"+m_parent.getLocale().getCountry()+"/" +t;
			    	if (!this.hasResource(resource)) {
			    		resource=m_resourcepath+m_parent.getLocale().getLanguage()+"/"+t;
			    	}
	    		} else {
	    			resource=m_resourcepath+m_parent.getLocale().getLanguage()+"/"+t;
	    		}
		    	if (!this.hasResource(resource)) {
		    		resource=m_resourcepath+t;
		    	}
	    	}
    	}
    	if (!this.hasResource(resource)) {
    		throw new Exception("Resource "+t + "not found");
    	}
    	//System.out.println("USE RESSOURCE:" + resource);
    	return resource;
    	
    }
    private boolean hasResource(String resource) {
    	boolean b= (this.getClass().getResource(resource)!=null);
    	return b;
    }
}
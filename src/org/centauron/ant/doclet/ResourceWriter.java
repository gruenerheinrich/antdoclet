package org.centauron.ant.doclet;

import java.io.File;
import java.io.FileInputStream;
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
        StringBuffer template = new StringBuffer();
        InputStream impstream = getPossiblyLocalizedResource(resource);
        if (impstream==null) {
        	System.out.println("RESOURCE IS NULL");
        }
        byte[] buff = new byte[10000];
        int size = 0;

        while( (size = impstream.read( buff )) > 0 )
            template.append( new String( buff, 0, size ) );        
        String format = MessageFormat.format( template.toString(), arguments );
        out.write( format.getBytes() );
    	
    }

    protected void write( OutputStream out, String resource ) throws Exception
    {
        InputStream impstream =getPossiblyLocalizedResource(resource);
        byte[] buff = new byte[10000];
        int size = 0;

        while( (size = impstream.read( buff )) > 0 )
            out.write( buff, 0, size );
    };

    public InputStream getPossiblyLocalizedResource(String t) throws Exception {
    	InputStream res=null;
    	if (getUserResourcePath()!=null) {
    		res=getPossiblyLocalizedResource(getUserResourcePath(),t,true,false);
    	}
    	if (res==null) {
    		res=getPossiblyLocalizedResource(m_resourcepath,t,false,true);
    	}
    	return res;
    }
    private String getUserResourcePath() {
		return m_parent.getUserResourcePath();
	}
	public InputStream getPossiblyLocalizedResource(String respath,String t,boolean isfileresource,boolean throwerror) throws Exception {
     	String resource=respath+t;
    	if (m_parent!=null) {
	    	if (m_parent.getLocale()!=null) {
	    		if (m_parent.getLocale().getCountry().length()>0) {
		    		resource=respath+m_parent.getLocale().getLanguage()+"/"+m_parent.getLocale().getCountry()+"/" +t;
			    	if (!this.hasResource(resource,isfileresource)) {
			    		resource=respath+m_parent.getLocale().getLanguage()+"/"+t;
			    	}
	    		} else {
	    			resource=respath+m_parent.getLocale().getLanguage()+"/"+t;
	    		}
		    	if (!this.hasResource(resource,isfileresource)) {
		    		resource=respath+t;
		    	}
	    	}
    	}
    	if (!this.hasResource(resource,isfileresource)) {
    		if (throwerror) {
    			throw new Exception("Resource "+t + " not found!");
    		} else {
    			
    			return null;
    		}
    	}
    	//System.out.println("USE RESSOURCE:" + resource);
    	if (isfileresource) {
    		return new FileInputStream(new File(resource));
    	} else {
    		return this.getClass().getResourceAsStream(resource);
    	}    	
    }
    private boolean hasResource(String resource,boolean isfileresource) {
    	if (isfileresource) {
    		return (new File(resource)).exists();
    	}
    	return (this.getClass().getResource(resource)!=null);
    }
}
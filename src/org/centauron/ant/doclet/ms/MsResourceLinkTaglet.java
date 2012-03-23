package org.centauron.ant.doclet.ms;

import com.sun.tools.doclets.Taglet;
import com.sun.javadoc.*;
import java.util.Map;


public class MsResourceLinkTaglet implements Taglet {

    private static final String NAME = "@ms.resourcelink";
    private String ms_location;
    public MsResourceLinkTaglet() {
    	//GET PATH TO MS    	
    	ms_location = System.getenv("MS");
    }
    public String getName() {
        return NAME;
    }

    public boolean inField() {
        return true;
    }

    public boolean inConstructor() {
        return true;
    }

    public boolean inMethod() {
        return true;
    }
    public boolean inOverview() {
        return true;
    }

    public boolean inPackage() {
        return false;
    }

    public boolean inType() {
        return true;
    }

    public boolean isInlineTag() {
        return true;
    }

    public static void register(Map tagletMap) {
       MsResourceLinkTaglet tag = new MsResourceLinkTaglet();
       Taglet t = (Taglet) tagletMap.get(tag.getName());
       if (t != null) {
           tagletMap.remove(tag.getName());
       }
       tagletMap.put(tag.getName(), tag);
    }

    public String toString(Tag tag) {
    	String descr=tag.text();
    	String location=ms_location+"/mdl/include/"+descr;
        return "<a href=\""+ location +"\"><code>" + descr + "</code></a>";
    }

    public String toString(Tag[] tags) {
        return null;
    }
}
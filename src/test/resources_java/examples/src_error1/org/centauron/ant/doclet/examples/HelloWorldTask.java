package org.centauron.ant.doclet.examples;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;

import javax.swing.*;

/**
 * @ant.description
 * This is an example Task, that shows how antdoclet works. 
 * @ant.description.de
 * Das ist eine Beispieltask, an dem die Funktionsweise von antdoclet erläutert wird. 
 * 
 * @ant.example.code
 * <helloworld display="EXAMPLE"/>
 * @ant.example.description
 * Example codes are here. They will automatically rendered as in &gt;pre&lt; 
 * @ant.example.description.de
 * Beispielcodes hier.
 *
 * @ant.example.code
 * <helloworld display="CHILDEXAMPLE">
 * 		<helloworldchild/>
 * </helloworld>
 * @ant.example.description
 * Example codes are here. They will automatically rendered as in &gt;pre&lt; 
 * FEHLER:  ant.example.description.de
 * Beispielcodes hier.

 * @author centauron - Stefan Bernsdorf
 * @version 0.7
 */
public class HelloWorldTask extends Task {

    /**
     * @ant.description
     * Example Property.
     * @ant.optional
     * 
     * @ant.description.de
     * Beispieleigenschaft
     * @ant.optional.de Defaultwert ist keine.
     */
	
    public void setDisplay(String title) {

    }

    /**
     * @ant.nodoc 
     */
    public void setNondoc(String propertyName) {

    }


    public void execute() throws BuildException {
    }

    /**
     * @ant.description
     * Child property description comes here.
     * Add a reference {@link helloworldchild} here.
     *
     * @ant.description.de
     * Kinderelemente werden hier beschrieben.
     * Sie können eine Referenz auf den Task anlegen {@link helloworldchild}.
     */
    
	public void addHelloworldchild(HelloWorldChildTask cc) throws BuildException {    	

	}	    
    
}

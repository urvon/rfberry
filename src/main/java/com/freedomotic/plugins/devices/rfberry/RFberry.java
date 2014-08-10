/**
 *
 * Copyright (c) 2009-2013 Freedomotic team http://freedomotic.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Freedomotic; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.freedomotic.plugins.devices.rfberry;

import com.freedomotic.api.EventTemplate;
import com.freedomotic.api.Protocol;
import com.freedomotic.app.Freedomotic;
import com.freedomotic.exceptions.UnableToExecuteException;
import com.freedomotic.reactions.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class RFberry
        extends Protocol {

    private static final Logger LOG = Logger.getLogger(RFberry.class.getName());
    private String pluginPath = System.getProperty("user.dir") + "/framework/freedomotic-core/plugins/devices/" + this.shortName;
    final int POLLING_WAIT;

    public RFberry() {
        //every plugin needs a name and a manifest XML file
        super("RFberry", "/rfberry/rfberry-manifest.xml");
        
        
        
        
        
        //read a property from the manifest file below which is in
        //FREEDOMOTIC_FOLDER/plugins/devices/com.freedomotic.hello/hello-world.xml
        POLLING_WAIT = configuration.getIntProperty("time-between-reads", 2000);
        //POLLING_WAIT is the value of the property "time-between-reads" or 2000 millisecs,
        //default value if the property does not exist in the manifest
        setPollingWait(POLLING_WAIT); //millisecs interval between hardware device status reads
    }

    @Override
    protected void onShowGui() {
        /**
         * uncomment the line below to add a GUI to this plugin the GUI can be
         * started with a right-click on plugin list on the desktop frontend
         * (com.freedomotic.jfrontend plugin)
         */
        //bindGuiToPlugin(new HelloWorldGui(this));
    }

    @Override
    protected void onHideGui() {
        //implement here what to do when the this plugin GUI is closed
        //for example you can change the plugin description
        setDescription("My GUI is now hidden");
    }

    @Override
    protected void onRun() {
    	//test envoi signal RF
    	
        String[] params = new String[5];
        params[0] = "./radioEmission";
        params[1] = "0";
        params[2] = "123456";
        params[3] = "3";
        params[4] = "ON";
        
        try {
        	String workingdirectory = System.getProperty("user.dir");
        	System.out.println(workingdirectory);
        	
        	Runtime run = Runtime.getRuntime(); 

        	String[] cmd = {"/bin/sh", "-c","sudo " + pluginPath + "/./radioEmission 0 12325261 3 off"}; 
        	LOG.info("Lancement de la commande :" + cmd[2]);
        	//String[] cmd = {"/bin/sh", "-c",pluginPath + "/./test"}; 
        	//String[] cmd = {"/bin/sh", "-c","/home/ludo/workspace/freedomotic/plugins/" +
        			//"devices/rfberry/src/main/resources/ > /home/ludo/coucou.txt"};
        	//ProcessBuilder p = run.exec(cmd);
        	
        	ProcessBuilder pb = new ProcessBuilder(cmd);
            Process p = pb.start();
            try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	
        	
			LOG.info("le programme RadioEmission a retourné le code :" + p.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //at the end of this method the system waits POLLINGTIME 
        //before calling it again. The result is this log message is printed
        //every 2 seconds (2000 millisecs)
    }

    @Override
    protected void onStart() {
    	try {
    		Runtime.getRuntime().exec("chmod 777 "+ this.pluginPath + "/radioEmission");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        LOG.info("RFberry plugin is started");
    }

    @Override
    protected void onStop() {
        LOG.info("RFberry plugin is stopped ");
    }

    @Override
    protected void onCommand(Command c)
            throws IOException, UnableToExecuteException {
        LOG.info("RFberry plugin receives a command called " + c.getName() + " with parameters "
                + c.getProperties().toString());
    }

    @Override
    protected boolean canExecute(Command c) {
        //don't mind this method for now
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onEvent(EventTemplate event) {
        //don't mind this method for now
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
package org.jboss.tools.cordavasim.eclipse.callbacks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.ExternalProcessCallback;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.TransparentReader;
import org.jboss.tools.vpe.cordovasim.eclipse.launch.internal.CordovaSimLauncher;

public class CordovaSimRestartCallback implements ExternalProcessCallback{
	//if you change this parameter, see also @org.jbosstools.browsersim.ui.BrowserSim
	private static final String NOT_STANDALONE = BrowserSimLauncher.NOT_STANDALONE;	
	private static final String CORDOVASIM_RESTART_COMMAND = "org.jboss.tools.vpe.cordavasim.command.restart:"; //$NON-NLS-1$
	private static final String PARAMETERS_DELIMITER = "_PARAMETERS_DELIMITER_"; //$NON-NLS-1$

	
	@Override
	public String getCallbackId() {
		return CORDOVASIM_RESTART_COMMAND;
	}

	@Override
	public void call(String restartMessage, TransparentReader reader) throws IOException {
		String[] messageArray = restartMessage.split(PARAMETERS_DELIMITER);
		String parameterString = messageArray[1];
		
		if (parameterString != null && !parameterString.isEmpty()) {
			List<String> parameters = new ArrayList<String>();
			parameters.add(NOT_STANDALONE);
			parameters.addAll(Arrays.asList(parameterString.split(" "))); //$NON-NLS-1$
			CordovaSimLauncher.launchCordovaSim(parameters);
		} else {
			throw new IllegalArgumentException("String '" + restartMessage + "' does not contain " + PARAMETERS_DELIMITER); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}
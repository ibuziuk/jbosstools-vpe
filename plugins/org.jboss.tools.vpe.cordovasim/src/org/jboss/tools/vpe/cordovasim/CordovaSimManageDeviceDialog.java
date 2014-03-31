package org.jboss.tools.vpe.cordovasim;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferencesStorage;
import org.jboss.tools.vpe.browsersim.ui.ManageDevicesDialog;
import org.jboss.tools.vpe.cordovasim.model.preferences.CordavaSimSpecificPreferencesStorage;
import org.jboss.tools.vpe.cordovasim.model.preferences.CordovaSimSpecificPreferences;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class CordovaSimManageDeviceDialog extends ManageDevicesDialog {
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.RestartCallback */
	private static final String CORDOVASIM_RESTART_COMMAND = "org.jboss.tools.vpe.cordavasim.command.restart:"; //$NON-NLS-1$
	private static final String PARAMETERS_DELIMITER = "_PARAMETERS_DELIMITER_"; //$NON-NLS-1$
	
	public CordovaSimManageDeviceDialog(Shell parent, int style, CommonPreferences oldCommonPreferences,
			SpecificPreferences oldSpecificPreferences, String currentUrl) {
		super(parent, style, oldCommonPreferences, oldSpecificPreferences, currentUrl);
	}

	@Override
	protected SpecificPreferencesStorage getSpecificPreferencesStorage() {
		return CordavaSimSpecificPreferencesStorage.INSTANCE;
	}
	
	
	@Override
	protected SpecificPreferences create(String selectedDeviceId, boolean useSkins, boolean enableLiveReload, int liveReloadPort, boolean enableTouchEvents, boolean isJavaFx) {
		return new CordovaSimSpecificPreferences(selectedDeviceId, useSkins, enableLiveReload, liveReloadPort, enableTouchEvents,
				oldSpecificPreferences.getOrientationAngle(), getParent().getLocation(),
				((CordovaSimSpecificPreferences)oldSpecificPreferences).getCordovaBrowserLocation(),
				((CordovaSimSpecificPreferences)oldSpecificPreferences).getCordovaBrowserSize(), isJavaFx);
	}
	
	@Override
	protected void sendRestartCommand() {
		System.out.println(CORDOVASIM_RESTART_COMMAND + PARAMETERS_DELIMITER + CordovaSimArgs.getRootFolder() + " " + CordovaSimArgs.getStartPage()  //$NON-NLS-1$
				+ " -port " + CordovaSimArgs.getPort() //$NON-NLS-1$
				+ " -engine " + CordovaSimArgs.getCordovaEngineLocation() //$NON-NLS-1$
				+ " -version " + CordovaSimArgs.getCordovaVersion()); //$NON-NLS-1$		
	}
}

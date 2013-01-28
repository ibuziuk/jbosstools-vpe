package org.jboss.tools.vpe.browsersim.eclipse.util;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;

public class BrowserSimPostShutDownDestroyer implements IWorkbenchListener {

	Process browserSimProcess;

	public BrowserSimPostShutDownDestroyer(Process browserSimProcess) {
		this.browserSimProcess = browserSimProcess;
	}

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		browserSimProcess.destroy();
	}
}
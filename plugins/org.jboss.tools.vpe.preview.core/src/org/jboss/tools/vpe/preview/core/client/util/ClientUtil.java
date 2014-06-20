package org.jboss.tools.vpe.preview.core.client.util;

import static org.jboss.tools.vpe.preview.core.server.HttpConstants.HTTP;
import static org.jboss.tools.vpe.preview.core.server.HttpConstants.LOCALHOST;
import static org.jboss.tools.vpe.preview.core.server.HttpConstants.PROJECT_NAME;
import static org.jboss.tools.vpe.preview.core.server.HttpConstants.VIEW_ID;

import org.eclipse.core.resources.IFile;

public class ClientUtil {
	
	private ClientUtil() {
	}

	public static String formRequestUrl(IFile ifile, int port, int modelHolderId) {
		String projectName = ifile.getProject().getName();
		String projectRelativePath = ifile.getProjectRelativePath().toString();
		String url = HTTP + LOCALHOST + ":" + port + "/" + projectRelativePath + "?" + PROJECT_NAME + "=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					+ projectName + "&" + VIEW_ID + "=" + modelHolderId; //$NON-NLS-1$ //$NON-NLS-2$

		return url;
	}

}

package org.jboss.tools.vpe.cordovasim.servlet.util;

import java.io.File;

public class ServletUtil {
	public static final String APPLICATION_JAVASCRIPT_CONTENT_TYPE = "application/javascript";
	public static final String ETAG = "Etag";
	public static final String IF_NONE_MATCH = "If-None-Match";

	public static String generateEtag(File file) {
		if (file.exists()) {
			return String.valueOf(file.lastModified());
		}
		return null;
	}

}

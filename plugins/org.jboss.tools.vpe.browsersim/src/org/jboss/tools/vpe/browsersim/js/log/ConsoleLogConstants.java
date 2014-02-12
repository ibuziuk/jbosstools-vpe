/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.js.log;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class ConsoleLogConstants {
	private ConsoleLogConstants() {
	}

	// Browser Functions names
	public static final String BROSERSIM_CONSOLE_LOG = "browserSimConsoleLog"; //$NON-NLS-1$
	public static final String BROSERSIM_CONSOLE_INFO = "browserSimConsoleInfo"; //$NON-NLS-1$
	public static final String BROSERSIM_CONSOLE_WARN = "browserSimConsoleWarn"; //$NON-NLS-1$
	public static final String BROSERSIM_CONSOLE_ERROR = "browserSimConsoleError"; //$NON-NLS-1$

	// Are needed for saving original main console functions
	public static final String ORIGINAL_CONSOLE_LOG = "original1onsoleLog"; //$NON-NLS-1$
	public static final String ORIGINAL_CONSOLE_INFO = "originalConsoleInfo"; //$NON-NLS-1$
	public static final String ORIGINAL_CONSOLE_WARN = "originalConsoleWarn"; //$NON-NLS-1$
	public static final String ORIGINAL_CONSOLE_ERROR = "originalConsoleError"; //$NON-NLS-1$

}

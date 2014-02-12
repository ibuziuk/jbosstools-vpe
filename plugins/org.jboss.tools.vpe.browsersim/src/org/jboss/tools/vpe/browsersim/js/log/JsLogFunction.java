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

import org.eclipse.swt.browser.BrowserFunction;
import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserFunction;

/**
 * {@link BrowserFunction} for processing main js console methods (log, error,warn, info) </br> 
 *  and logging relevant messages to the eclipse console
 * 
 * @author Ilya Buziuk (ibuziuk)
 */
public class JsLogFunction implements IBrowserFunction {
	private IBrowser browser;
	private MessageType type;

	public JsLogFunction(IBrowser browser, MessageType type) {
		this.browser = browser;
		this.type = type;
	}

	@Override
	public Object function(Object[] arguments) {
		addTypeInfo(arguments, type);
		if (arguments != null && arguments.length >= 1) {
			StringBuffer message = new StringBuffer();
			
			for (Object argument : arguments) {
				message.append(argument.toString() + " "); //$NON-NLS-1$
			}
			JsConsoleLogger.log(message.toString());
			executeOriginalFunction(type, message.toString());
		}
		return null;
	}

	private void executeOriginalFunction(MessageType type, String message) {
		switch (type) {
		case INFO:
			browser.execute(ConsoleLogConstants.BROSERSIM_CONSOLE_INFO + "(" + message +")"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		
		case WARN:
			browser.execute(ConsoleLogConstants.BROSERSIM_CONSOLE_WARN + "(" + message +")"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		
		case ERROR:
			browser.execute(ConsoleLogConstants.BROSERSIM_CONSOLE_ERROR + "(" + message +")"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
			
		default: // console.log()
			browser.execute(ConsoleLogConstants.BROSERSIM_CONSOLE_LOG + "(" + message +")"); //$NON-NLS-1$ //$NON-NLS-2$
			break;
		}
	}

	public void addTypeInfo(Object[] arguments, MessageType type) {
		if (type != null && arguments != null && arguments.length >= 1) {
			arguments[0] = type.toString() + ": " + arguments[0]; //$NON-NLS-1$
		} 
	}
	
}

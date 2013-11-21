/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.cordovasim.plugins.inappbrowser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;
import org.jboss.tools.vpe.cordovasim.CustomBrowserSim;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class InAppBrowserLoader {

	@SuppressWarnings("nls")
	public static boolean isInAppBrowserEvent(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		return Boolean.TRUE.equals(parentBrowser.evaluate("return !!window.needToOpenInAppBrowser"));
	}
	
	@SuppressWarnings("nls")
	public static void processInAppBrowser(final Browser rippleToolSuiteBrowser, final CustomBrowserSim browserSim, WindowEvent openWindowEvent) {
		rippleToolSuiteBrowser.execute("window.needToOpenInAppBrowser = false"); 
		
		final Browser browserSimBrowser = browserSim.getBrowser();
		final Composite browserSimParentComposite = browserSimBrowser.getParent();
		final StackLayout stackLayout = (StackLayout) browserSimParentComposite.getLayout();
		Device device = browserSim.getCurrentDevice();
		
		final BrowserSimBrowser inAppBrowser = createInAppBrowser(browserSimParentComposite, browserSimBrowser, device); 
		browserSim.setInAppBrowser(inAppBrowser);

		stackLayout.topControl = inAppBrowser; // hiding browserSim's browser and showing inAppBrowser 
		openWindowEvent.browser = inAppBrowser;  
		browserSimParentComposite.layout();
			
		BrowserSimUtil.setCustomScrollbarStylesForWindows(inAppBrowser);
		
		inAppBrowser.addCloseWindowListener(new CloseWindowListener() {			
			
			@Override
			public void close(WindowEvent event) {
				browserSim.setInAppBrowser(null);
				stackLayout.topControl = browserSimBrowser;
				browserSimParentComposite.layout();	
				inAppBrowser.dispose();
				rippleToolSuiteBrowser.execute("ripple('event').trigger('browser-close');"); // fire 'exit' for inAppBrowser
				rippleToolSuiteBrowser.execute("ripple('emulatorBridge').window().ChildBrowser.onClose();"); // fire 'close' for childBrowser
			}
		});

		inAppBrowser.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
				rippleToolSuiteBrowser.execute("ripple('emulatorBridge').window().ChildBrowser.onLocationChange('"
						+ event.location + "');"); // fire 'ChildBrowser.onLocationChange' event
				rippleToolSuiteBrowser.execute("ripple('event').trigger('browser-start');"); // fire 'loadstart' event
			}
			
			@Override
			public void changed(LocationEvent event) {
				if (event.top) {
					rippleToolSuiteBrowser.execute("ripple('event').trigger('browser-stop');"); //  fire 'loadstop' event
				}
			}
			
		});
				
		new ExecScriptFunction(browserSimBrowser, inAppBrowser, "csInAppExecScript");
	}

	private static BrowserSimBrowser createInAppBrowser(Composite browserSimParentComposite, Browser browserSimBrowser,
			Device device) {
		BrowserSimBrowser inAppBrowser = new WebKitBrowserFactory().createBrowser(browserSimParentComposite, SWT.NONE);
		inAppBrowser.setDefaultUserAgent(device.getUserAgent());
		inAppBrowser.setLayoutData(browserSimBrowser.getLayoutData());
		return inAppBrowser;
	}
	
}


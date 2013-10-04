package org.jboss.tools.vpe.cordovasim.plugins.inappbrowser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Composite;

public class InAppBrowserLoader {

	public static boolean isInAppBrowserEvent(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		return Boolean.TRUE.equals(parentBrowser.evaluate("return !!window._csInAppBrowserOpen"));
	}
	
	public static void processInAppBrowser(final Browser rippleToolBarBrowser, final Browser browserSimBrowser, WindowEvent openWindowEvent) {
		rippleToolBarBrowser.execute("window._csInAppBrowserOpen = false");
		final Composite browserSimParentComposite = browserSimBrowser.getParent();
		final Browser inAppBrowser = new Browser(browserSimParentComposite, SWT.WEBKIT);
		inAppBrowser.setLayoutData(browserSimBrowser.getLayoutData());
		browserSimBrowser.setParent(inAppBrowser); // just change browsers parent for hiding browserSim's browser
		
		openWindowEvent.browser = inAppBrowser;  
		browserSimParentComposite.layout();
	
		inAppBrowser.addCloseWindowListener(new CloseWindowListener() {			
			
			@Override
			public void close(WindowEvent event) {
				browserSimBrowser.setParent(browserSimParentComposite);
				inAppBrowser.dispose();
				browserSimParentComposite.layout();		
				rippleToolBarBrowser.execute("ripple('event').trigger('browser-close');"); // fire 'exit'
			}
		});
		
		inAppBrowser.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
				rippleToolBarBrowser.execute("ripple('event').trigger('browser-start');"); // fire 'loadstart'
			}
			
			@Override
			public void changed(LocationEvent event) {
				rippleToolBarBrowser.execute("ripple('event').trigger('browser-stop');"); // fire 'loadstop'
			}
		});
				
		new ExecScriptFunction(browserSimBrowser, inAppBrowser, "csInAppExecScript");
	}

}


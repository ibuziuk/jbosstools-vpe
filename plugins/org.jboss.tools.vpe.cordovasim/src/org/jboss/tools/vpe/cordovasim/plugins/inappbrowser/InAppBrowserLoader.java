package org.jboss.tools.vpe.cordovasim.plugins.inappbrowser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

public class InAppBrowserLoader {

	public static boolean isInAppBrowserEvent(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		return Boolean.TRUE.equals(parentBrowser.evaluate("return !!window.needToOpenInAppBrowser"));
	}
	
	public static void processInAppBrowser(final Browser rippleToolBarBrowser, BrowserSim browserSim, WindowEvent openWindowEvent) {
		rippleToolBarBrowser.execute("window.needToOpenInAppBrowser = false"); 
		
		final Browser browserSimBrowser = browserSim.getBrowser();
		final Composite browserSimParentComposite = browserSimBrowser.getParent();
		Device device = browserSim.getCurrentDevice();
		final BrowserSimBrowser inAppBrowser = createInAppBrowser(browserSimParentComposite, browserSimBrowser, device); 
		
		browserSimBrowser.setParent(inAppBrowser); // hiding browserSim's browser by changing it's parent   
		openWindowEvent.browser = inAppBrowser;  
		browserSimParentComposite.layout();
	
		inAppBrowser.addCloseWindowListener(new CloseWindowListener() {			
			
			@Override
			public void close(WindowEvent event) {
				browserSimBrowser.setParent(browserSimParentComposite);
				inAppBrowser.dispose();
				browserSimParentComposite.layout();		
				rippleToolBarBrowser.execute("ripple('event').trigger('browser-close');"); // fire 'exit' event
			}
		});
		
		inAppBrowser.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				browserSimBrowser.setParent(browserSimParentComposite);
				browserSimParentComposite.layout();		
			}
		});
		
		inAppBrowser.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
//				if (event.top) {
					if (isChildBrowserPluginPlugged(rippleToolBarBrowser)) {
						rippleToolBarBrowser
								.execute("ripple('emulatorBridge').window().ChildBrowser.onLocationChange('"
										+ event.location + "');"); // fire 'ChildBrowser.onLocationChange' event
					} else {
						rippleToolBarBrowser.execute("ripple('event').trigger('browser-start');"); // fire 'loadstart' event
					}
//				}
			}
			
			@Override
			public void changed(LocationEvent event) {
				if (event.top) {
					Browser browser = (Browser) event.widget;
					BrowserSimUtil.setCustomScrollbarStyles(browser);
					rippleToolBarBrowser.execute("ripple('event').trigger('browser-stop');"); //  fire 'loadstop' event
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

	private static boolean isChildBrowserPluginPlugged(Browser browser) {
		return (Boolean) browser.evaluate("return !! ripple('emulatorBridge').window().ChildBrowser");
	}

}


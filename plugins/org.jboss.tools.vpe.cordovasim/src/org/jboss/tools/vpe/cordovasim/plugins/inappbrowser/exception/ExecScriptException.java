package org.jboss.tools.vpe.cordovasim.plugins.inappbrowser.exception;

public class ExecScriptException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExecScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecScriptException(String message) {
		super(message);
	}

	public ExecScriptException(Throwable cause) {
		super(cause);
	}

	public ExecScriptException() {
		super();
	}

}

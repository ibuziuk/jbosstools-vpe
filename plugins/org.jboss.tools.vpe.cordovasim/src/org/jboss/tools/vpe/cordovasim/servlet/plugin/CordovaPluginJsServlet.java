package org.jboss.tools.vpe.cordovasim.servlet.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.tools.vpe.cordovasim.plugin.exception.PluginJsException;
import org.jboss.tools.vpe.cordovasim.plugin.model.PluginIdCache;
import org.jboss.tools.vpe.cordovasim.plugin.model.Plugin;
import org.jboss.tools.vpe.cordovasim.plugin.util.CordovaPluginXmlUtil;
import org.jboss.tools.vpe.cordovasim.plugin.util.CordovaFileUtil;
import org.jboss.tools.vpe.cordovasim.servlet.util.ServletUtil;

public class CordovaPluginJsServlet extends HttpServlet {
	private File pluginDir;

	public CordovaPluginJsServlet(File pluginDir) {
		super();
		this.pluginDir = pluginDir;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ifNoneMatchValue = req.getHeader(ServletUtil.IF_NONE_MATCH);
		String eTag = ServletUtil.generateEtag(pluginDir);

		if ((ifNoneMatchValue != null) && (eTag.equals(ifNoneMatchValue))) {
			resp.setHeader(ServletUtil.ETAG, eTag);
			resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			try {
				List<File> pluginXmlFiles = CordovaFileUtil.getPluginXmlFiles(pluginDir); // get all plugin.xml files from the "plugins" folder of the hybrid project
				List<Plugin> plugins = CordovaPluginXmlUtil.getPluginsfromFiles(pluginXmlFiles); // generate Plugins from the plugin.xml files

				PluginIdCache.update(plugins); // Cache with mapping plugin's file to id is needed in PluginServlet
				String content = CordovaFileUtil.generateCordovaPluginsJsContent(plugins);

				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType(ServletUtil.APPLICATION_JAVASCRIPT_CONTENT_TYPE);
				resp.setHeader(ServletUtil.ETAG, eTag);
				resp.getWriter().write(content);
			} catch (PluginJsException e) { // TODO log the exception
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}

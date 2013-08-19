package org.jboss.tools.vpe.cordovasim.plugin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jetty.util.ajax.JSON;
import org.jboss.tools.vpe.cordovasim.plugin.model.Plugin;

public class CordovaFileUtil {
	private static final String PLUGINS_DIR = "plugins";
	private static final String CORDOVA = ".cordova";
	private static final String CONFIG_JSON = "config.json";
	private static final String LIB = "lib";
	private static final String WWW = "www";
	private static final String VERSION = "version";
	private static final String FILE = "\"file\": ";
	private static final String ID = "\"id\": ";
	private static final String CLOBBERS = "\"clobbers\": [\n";
	private static final String MERGES = "\"merges\": [\n";
	private static final String PLUGIN_XML = "plugin.xml";
	private static final String CORDOVA_PLUGINS_JS_BEGINNING = "cordova.define('cordova/plugin_list', function(require, exports, module) { \n module.exports = [\n";
	private static final String CORDOVA_PLUGINS_JS_END = "]\n});";
	private static final String CORDOVA_DEFINE = "cordova.define(";
	private static final String FUNCTION_BEGINNING = "function(require, exports, module) {\n";
	private static final String FUNCTION_END = "});";

	public static List<File> getPluginXmlFiles(File pluginsDir) {
		List<File> pluginXmlFiles = new ArrayList<File>();
		File[] pluginDirs = pluginsDir.listFiles();
		for (File pluginDir : pluginDirs) {
			if (pluginDir.isDirectory()) {
				File pluginXmlFile = new File(pluginDir, PLUGIN_XML);
				if (pluginDir.exists()) {
					pluginXmlFiles.add(pluginXmlFile);
				}
			}
		}

		return pluginXmlFiles;
	}

	public static String generateCordovaPluginsJsContent(List<Plugin> plugins) {
		String pluginContent = "";
		Iterator<Plugin> pluginIterator = plugins.iterator();
		while (pluginIterator.hasNext()) {
			Plugin plugin = pluginIterator.next();
			pluginContent += "\n\t{\n";
			pluginContent += "\t\t" + FILE + "\"" + plugin.getFile() + "\",\n";
			pluginContent += "\t\t" + ID + "\"" + plugin.getId() + "\",\n";
			Iterator<String> mapperIterator = null;
			if (plugin.getClobbers().size() > 0) {
				pluginContent += "\t\t" + CLOBBERS;
				mapperIterator = plugin.getClobbers().iterator();
			} else if (plugin.getMerges().size() > 0) {
				pluginContent += "\t\t" + MERGES;
				mapperIterator = plugin.getMerges().iterator();
			}
			while (mapperIterator.hasNext()) {
				String clobber = mapperIterator.next();
				pluginContent += "\t\t\t\"" + clobber + "\"";
				if (mapperIterator.hasNext()) {
					pluginContent += ",\n";
				} else {
					pluginContent += "\n\t\t]";
				}
			}
			if (pluginIterator.hasNext()) {
				pluginContent += "\n\t},";
			} else {
				pluginContent += "\n\t}\n";
			}
		}
		return CORDOVA_PLUGINS_JS_BEGINNING + pluginContent + CORDOVA_PLUGINS_JS_END;
	}

	public static String generatePluginContent(File file, String pluginId) throws FileNotFoundException {
		String content = null;
		if (file.exists()) {
			String fileContent = new Scanner(file).useDelimiter("\\A").next();
			content = CORDOVA_DEFINE + '"' + pluginId + '"' + ", " + FUNCTION_BEGINNING + fileContent + FUNCTION_END;
		}
		return content;
	}
	
	public static File getPluginDir(String resourceBase) {
		File parentDir = getParentDir(resourceBase);
		if (parentDir != null) {
			File pluginDir = new File(parentDir, PLUGINS_DIR);
			if (pluginDir.exists()) {
				return pluginDir;
			}
		}
		return null;
	}

	public static File getConfigJson(String resourceBase) { 
		File parentDir = getParentDir(resourceBase);	
		if (parentDir != null) {
			File cordovaDir = new File(parentDir, CORDOVA);
			if (cordovaDir.exists()) {
				File configJson = new File(cordovaDir, CONFIG_JSON);
					if (configJson.exists()) {
						return configJson;
				}
			}
		}
		return null;
	}
	
	
	public static String getCordovaVersion(String resourseBase) throws FileNotFoundException { // TODO - need to do this better
		File configJson = getConfigJson(resourseBase);
		if (configJson != null) {
			String content = new Scanner(configJson).useDelimiter("\\A").next();
			Map map = (Map) JSON.parse(content);
			Map lib = (Map) map.get(LIB);
			Map www = (Map) lib.get(WWW);
			String version = (String) www.get(VERSION);
			return version;
		}
		return null;
	}

	private static File getParentDir(String childDir) {
		File file = new File(childDir);
		if (file.exists()) {
			return file.getParentFile();
		}
		return null;
	}
	
}
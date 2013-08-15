package org.jboss.tools.vpe.cordovasim.plugin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginIdCache {
	private static final Map<String, String> FILE_TO_ID_CACHE = new HashMap<String, String>(); // Maps plugin's "file" to "id"

	private PluginIdCache() {
	}

	public static void update(List<Plugin> plugins) {
		FILE_TO_ID_CACHE.clear();
		for (Plugin plugin : plugins) {
			FILE_TO_ID_CACHE.put(plugin.getFile(), plugin.getId());
		}
	}

	public static String getPluginId(String fileName) {
		return FILE_TO_ID_CACHE.get(fileName);
	}

}

package com.freecharge.wordcount.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.glassfish.grizzly.PortRange;

public class SystemConfig {
	
	private static final String CONFIG_FILE = "word-count.conf";
	private Map<String, String> properties;
	private static SystemConfig INSTANCE;
	private SystemConfig(){
		
	}
	
	public static SystemConfig getSystemConfig() {
		if(INSTANCE == null) {
			synchronized (INSTANCE) {
				if(INSTANCE == null) {
					INSTANCE = new SystemConfig();
					Properties props = new Properties();
					try {
						props.load(new FileInputStream(new File(CONFIG_FILE)));
						for (Entry<Object, Object> prop : props.entrySet()) {
							INSTANCE.properties.put(String.valueOf(prop.getKey()), 
									String.valueOf(prop.getValue()));
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return INSTANCE;
	}
	
	public int getThreadPoolSize() {
		return Integer.valueOf(properties.get("thread.pool.size"));
	}

	public String getFileCorpusLocation() {
		return properties.get("file.corpus.location");
	}

	public int getServerPort() {
		try {
			return Integer.valueOf(properties.get("http.server.port"));
		}
		catch(NumberFormatException e) {
			return 80;
		}
	}

}

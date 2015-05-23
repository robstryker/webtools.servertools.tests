package org.eclipse.wst.server.core.tests.integration.model;

import java.util.HashMap;

import org.eclipse.wst.server.core.IServer;

public class DataModel {
	private static DataModel instance = new DataModel();
	public static DataModel getInstance() {
		return instance;
	}
	
	public static final String PUBLISH_TIME = "publishTime";
	public static final String START_TIME = "startTime";
	
	
	private HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>(); 
	
	public void setProperty(IServer server, String key, Object val) {
		HashMap<String ,Object> m2 = map.get(server.getId());
		if( m2 == null ) {
			m2 = new HashMap<String, Object>();
			map.put(server.getId(), m2);
		}
		m2.put(key, val);
	}
	
	public <T> T  getProperty(IServer server, String key, Class<T> c) {
		return (T)map.get(server.getId()).get(key);
	}

	public void clearProperties(IServer s) {
		map.remove(s.getId());
	}
	
}

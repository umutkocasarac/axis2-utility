package com.umut.pool.axis2.util;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

/**
 * 
 * @author umut.kocasarac
 *
 */

public class Axis2Utility {
	private static final Logger log = Logger.getLogger(Axis2Utility.class);

	private Axis2Utility() {
	}

	public static final String CLIENT_AXIS2_XML = System.getProperty("AXIS2_CLIENT_CONF");
	private static ConfigurationContext configurationContext;
	private static Object lock = new Object();
	
	public static ConfigurationContext getConfigurationContext() {
		if (configurationContext == null) {
			try {
				synchronized (lock) {
					if (configurationContext == null) {
						configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, CLIENT_AXIS2_XML);
						MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager(); 

			            HttpConnectionManagerParams params = new HttpConnectionManagerParams(); 
			            params.setDefaultMaxConnectionsPerHost(500);
						params.setMaxTotalConnections(15000);
			            multiThreadedHttpConnectionManager.setParams(params); 
			            HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager); 
			            configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient); 
					}
				}

			} catch (AxisFault e) {
				log.error(e.getMessage(), e);
			}
		}
		return configurationContext;
	}
}

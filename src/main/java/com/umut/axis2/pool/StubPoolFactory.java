package com.umut.axis2.pool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

import com.umut.pool.axis2.util.Axis2Utility;

/**
 * 
 * @author umut.kocasarac
 *
 */
public class StubPoolFactory implements KeyedPooledObjectFactory<StubProperties, Stub>{
	private static final Logger log = Logger.getLogger(StubPoolFactory.class);
	
	public PooledObject<Stub> makeObject(StubProperties key) throws Exception {
		DefaultPooledObject<Stub> pooledStub = null;
		try {
			Constructor<? extends Stub> constructor = key.getClassName().getConstructor(ConfigurationContext.class, String.class);
			Stub stub = constructor.newInstance(Axis2Utility.getConfigurationContext(), key.getEpr());
			pooledStub = new DefaultPooledObject<Stub>(stub); 
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return pooledStub;
	}

	public void destroyObject(StubProperties key, PooledObject<Stub> p) throws Exception {
		log.debug("destroy object called " + key.toString());
		
	}

	public boolean validateObject(StubProperties key, PooledObject<Stub> p) {
		log.debug("validate object called " + key.toString());
		return false;
	}

	public void activateObject(StubProperties key, PooledObject<Stub> p) throws Exception {
		log.debug("activate object called " + key.toString());
	}

	public void passivateObject(StubProperties key, PooledObject<Stub> p) throws Exception {
		log.debug("passivate object called " + key.toString());
		p.getObject()._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_HEADERS, null);
	}




}

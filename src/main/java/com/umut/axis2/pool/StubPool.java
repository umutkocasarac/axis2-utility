package com.umut.axis2.pool;

import java.util.NoSuchElementException;

import org.apache.axis2.client.Stub;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.log4j.Logger;

import com.umut.axis2.pool.exception.NotAvailable;

/**
 * 
 * @author umut.kocasarac
 *
 */
public class StubPool {
	private static final Logger log = Logger.getLogger(StubPool.class);
	private static GenericKeyedObjectPool<StubProperties, Stub> genericKeyedObjectPool;

	static {
		StubPoolFactory stubPoolFactory = new StubPoolFactory();
		GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
		config.setMaxTotalPerKey(getMaxTotalPerKey());
		config.setMaxWaitMillis(getMaxWaitMillis());
		genericKeyedObjectPool = new GenericKeyedObjectPool<StubProperties, Stub>(stubPoolFactory, config);
	}
	
	private static int getMaxTotalPerKey(){
		String propertyValue = System.getProperty("Pool.MaxTotalPerKey");
		int maxTotalPerKey = 15;
		if(null != propertyValue){
			try {
				maxTotalPerKey = Integer.valueOf(propertyValue);
			} catch (NumberFormatException e) {
				log.warn("Pool.MaxTotalPerKey is set to default value");
			}
		}
		return maxTotalPerKey;
	}
	
	private static int getMaxWaitMillis(){
		String propertyValue = System.getProperty("Pool.MaxWaitMillis");
		int maxWaitMillis = 100;
		if(null != propertyValue){
			try {
				maxWaitMillis = Integer.valueOf(propertyValue);
			} catch (NumberFormatException e) {
				log.warn("Pool.MaxWaitMillis is set to default value");
			}
		}
		return maxWaitMillis;
	}

	public static Stub getStub(Class<? extends Stub> className, String epr) {
		return getStub(className, epr, true);
	}

	public static Stub getStub(Class<? extends Stub> className, String epr, boolean reTry) {
		Stub stub = null;
		StubProperties stubProperties = new StubProperties(className, epr);
		try {
			stub = genericKeyedObjectPool.borrowObject(stubProperties);
		} catch (NoSuchElementException e) {
			log.error("Class =" + className + " epr =" + epr);
			logError(e, stubProperties);
			if (reTry) {
				stub = clearAndRetry(className, epr);
			} else {
				throw new NotAvailable();
			}
		} catch (Exception e) {
			log.error("Class =" + className + " epr =" + epr);
			logError(e, stubProperties);
			if (reTry) {
				stub = clearAndRetry(className, epr);
			} else {
				throw new NotAvailable();
			}
		}
		return stub;
	}

	private static Stub clearAndRetry(Class<? extends Stub> className, String epr) {
		genericKeyedObjectPool.clearOldest();
		return getStub(className, epr, false);
	}

	private static void logError(Exception e, StubProperties key) {
		log.error(e.getMessage(), e);
		log.info("Waiter count =" + genericKeyedObjectPool.getNumWaiters());
		log.info("Active count =" + genericKeyedObjectPool.getNumActive(key));
		log.info("Idle count =" + genericKeyedObjectPool.getNumIdle(key));
		StackTraceElement[] myCaller = Thread.currentThread().getStackTrace();
		int i = 0;
		for (StackTraceElement s : myCaller) {
			log.info(i++ + " Stack Trace =" + s.getClassName());
		}
	}

	public static void releaseStub(Class<? extends Stub> className, String epr, Stub serviceStub) {
		try{
			genericKeyedObjectPool.returnObject(new StubProperties(className, epr), serviceStub);
		}catch(IllegalStateException e){
			log.error("Object already return to pool");
			StackTraceElement[] myCaller = Thread.currentThread().getStackTrace();
			int i = 0;
			for (StackTraceElement s : myCaller) {
				log.info(i++ + " Release Stack Trace =" + s.getClassName());
			}
		}
	}

}

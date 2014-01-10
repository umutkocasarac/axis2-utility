package com.umut.axis2.pool;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.Stub;
import org.apache.commons.httpclient.Header;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class StubPoolTest{
	
    @Before
	public void init(){
		
	}
    @SuppressWarnings("unchecked")
	@Test
	public void testPool() throws Exception{
    	String serviceName = "TestService";
    	String serverUrl =  "testurl";
        String serviceUrl = serverUrl + serviceName;
		Stub s1 = StubPool.getStub(TestStub.class, serviceUrl);
		setHeaders(s1, "s1");
		Object s1Header = getHeaders(s1);
		Assert.assertEquals( ((List<Header> ) s1Header).get(0).getValue(), "s1");
		StubPool.releaseStub(TestStub.class, serviceUrl, s1);
		Stub s2 = StubPool.getStub(TestStub.class, serviceUrl);
		Object s2Header = getHeaders(s1);
		Assert.assertNull(s2Header);
		Assert.assertEquals(System.identityHashCode(s1), System.identityHashCode(s2));
		Stub s3 = StubPool.getStub(TestStub.class, serviceUrl);
		Assert.assertNotEquals(System.identityHashCode(s3), System.identityHashCode(s2));
	}
    
    private static void setHeaders(Stub stub, String sessionId) {
    	List<Header> headerList = new ArrayList<Header>();
		Header sessionIdHeader = new Header("sessionId", sessionId);
		headerList.add(sessionIdHeader);

		Options options = stub._getServiceClient().getOptions();
		options.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_HEADERS,headerList);
	}
    
    private static Object getHeaders(Stub stub) {
		Options options = stub._getServiceClient().getOptions();
		return options.getProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_HEADERS);
	}
}

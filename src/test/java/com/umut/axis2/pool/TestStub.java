package com.umut.axis2.pool;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;

/**
 * 
 * @author umut.kocasarac
 *
 */
public class TestStub extends Stub {

	public TestStub(ConfigurationContext configurationContext, String targetEndpoint) throws AxisFault {
		this(configurationContext, targetEndpoint, false);
	}

	public TestStub(ConfigurationContext configurationContext, String targetEndpoint, boolean useSeparateListener) throws AxisFault {
		this._serviceClient = new ServiceClient(configurationContext, this._service);

		this._serviceClient.getOptions().setTo(new EndpointReference(targetEndpoint));

		this._serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

		this._serviceClient.getOptions().setSoapVersionURI("http://www.w3.org/2003/05/soap-envelope");
	}

	public TestStub(ConfigurationContext configurationContext) throws AxisFault {
		this(configurationContext, "https://testurl.com/services/testService/");
	}

	public TestStub() throws AxisFault {
		this("https://testurl.com/services/testService/");
	}

	public TestStub(String targetEndpoint) throws AxisFault {
		this(null, targetEndpoint);
	}

}

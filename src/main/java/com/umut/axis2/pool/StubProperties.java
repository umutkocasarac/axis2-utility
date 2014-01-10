package com.umut.axis2.pool;

import org.apache.axis2.client.Stub;

/**
 * 
 * @author umut.kocasarac
 *
 */
public class StubProperties {
	private Class<? extends Stub> className;
	private String epr;
	
	public StubProperties(Class<? extends Stub> className, String epr) {
		super();
		this.className = className;
		this.epr = epr;
	}
	public String getEpr() {
		return epr;
	}
	public void setEpr(String epr) {
		this.epr = epr;
	}
	public Class<? extends Stub> getClassName() {
		return className;
	}
	public void setClassName(Class<? extends Stub> className) {
		this.className = className;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((epr == null) ? 0 : epr.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StubProperties other = (StubProperties) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (epr == null) {
			if (other.epr != null)
				return false;
		} else if (!epr.equals(other.epr))
			return false;
		return true;
	}
	
}

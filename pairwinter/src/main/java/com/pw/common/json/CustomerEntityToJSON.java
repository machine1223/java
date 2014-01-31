package com.pw.common.json;

import org.codehaus.jackson.node.ObjectNode;

/**
 * 
 * CustomerEntityToJSON.<br/>
 * 
 * @author carl
 * @since  Jun 5, 2012 
 * @param <T>
 */
public interface CustomerEntityToJSON<T> {
	public ObjectNode getObjectNode(T t);
}

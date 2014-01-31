package com.pw.common.json;

import org.codehaus.jackson.node.ObjectNode;

import java.util.List;

/**
 * User: carl
 * Date: 5/20/13
 * Time: 3:40 PM
 */
public interface CustomerEntityToJSONArray<T> {
    public List<ObjectNode> getObjectNode(T t);
}

package com.pw.common.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.List;

/**
 * 
 * CustomerJSONSerializer.<br/>
 *
 * @author carl
 * @since  Jun 5, 2012 
 * @param <T>
 */
public class CustomerJSONSerializer<T> extends SerializerBase<T> {
	private CustomerEntityToJSON<T> cetj;
	private CustomerEntityToJSONArray<T> cetja;
    private boolean isArray = false;
	public CustomerJSONSerializer(Class<T> t, CustomerEntityToJSON<T> cetj) {
		super(t);
		this.cetj = cetj;
	}

    public CustomerJSONSerializer(Class<T> t, CustomerEntityToJSONArray<T> cetja) {
        super(t);
        this.cetja = cetja;
        this.isArray = true;
    }

	@Override
	public void serialize(T value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
        if(isArray){
            List<ObjectNode> nodes = cetja.getObjectNode(value);
            for(ObjectNode objectNode: nodes){
                jgen.writeTree(objectNode);
            }
        }else{

            jgen.writeTree(cetj.getObjectNode(value));
        }
	}
	
	public CustomerJSONSerializer(Class<T> t, boolean dummy) {
		super(t, dummy);
		// TODO Auto-generated constructor stub
	}

	public CustomerJSONSerializer(Class<T> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	public CustomerJSONSerializer(JavaType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	
}

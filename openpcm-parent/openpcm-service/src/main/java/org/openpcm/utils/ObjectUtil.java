package org.openpcm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public static String print(Object object) {
		try {
            return OBJECT_MAPPER.writer().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
	}
	
	public static String prettyPrint(Object object) {
		try {
            return OBJECT_MAPPER.writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
	}
	
	private ObjectUtil() {
		
	}
}

package org.openpcm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public final class ObjectUtil {

	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

	@Autowired
	public void setObjectMapper(ObjectMapper mapper) {
		ObjectUtil.OBJECT_MAPPER = mapper;
	}

	private ObjectUtil() {

	}
}

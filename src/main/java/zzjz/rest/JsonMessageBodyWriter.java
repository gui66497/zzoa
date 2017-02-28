package zzjz.rest;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonMessageBodyWriter implements MessageBodyWriter {

	public boolean isWriteable(Class type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	public long getSize(Object t, Class type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(Object target, Class type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, OutputStream outputStream)
			throws IOException, WebApplicationException {
		new ObjectMapper().writeValue(outputStream, target);		
	}
}
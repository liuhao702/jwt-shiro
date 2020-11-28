package code.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 转json
 */
public final class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();
 
    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }

    public static <T> String obj2String(T t) {
        if (t == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error on obj2String", e);
        }
    }
 
    public static <T> String obj2StringPretty(T t) {
        if (t == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error on obj2StringPretty", e);
        }
    }
 
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return objectMapper.readValue(str, clazz);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        }
    }
 
    public  static  <T> T string2Obj(String str, TypeReference<T> reference) {
        if (StringUtils.isEmpty(str) || reference == null) {
            return null;
        }
        try {
            return objectMapper.readValue(str, reference);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>...elecmentClassess) {
        JavaType javatype = objectMapper.getTypeFactory().constructParametricType(collectionClass, elecmentClassess);
        try {
            return objectMapper.readValue(str, javatype);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on string2Obj", e);
        }
    }
}
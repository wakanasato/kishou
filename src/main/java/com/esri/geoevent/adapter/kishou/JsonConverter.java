package com.esri.geoevent.adapter.kishou;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * @author Esri Japan
 * Json Converter Class
 */
public class JsonConverter {

    private static ObjectMapper mapper = new ObjectMapper();

    public JsonConverter() {
        // �����͂Ȃ�
        ;
    }

    public static String toString(final Object object) throws JsonGenerationException, JsonMappingException,
            IOException {

        String json = mapper.writeValueAsString(object);

        return json;
    }

    public static <T> T toObject(final String jsonString, final Class<T> clazz) throws JsonParseException,
            JsonMappingException, IOException {

        T object = null;

        if (jsonString == null) {
            throw new InvalidParameterException("jsonString is null.");
        }
        object = mapper.readValue(jsonString, clazz);

        return object;
    }

    public static <T> T toObject(String jsonString,
                                 TypeReference<T> valueTypeRef) throws JsonParseException, JsonMappingException, IOException {

        T object = null;

        if (jsonString == null) {
            throw new InvalidParameterException("jsonString is null.");
        }
        object = mapper.readValue(jsonString, valueTypeRef);

        return object;
    }


    public static ObjectNode toJsonObject(Object bean) throws IOException, JsonProcessingException {
//      InfoBean オブジェクトを Json 文字列に変換
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
//      Json 文字列を Json オブジェクトに変換 （InfoBean を直接 Json オブジェクトにするメソッドはない模様）
        ObjectNode json = (ObjectNode) mapper.readTree(jsonString);
        return json;
    }

//    public static ArrayNode toJsonArray (String jsonString) throws IOException {
//        ObjectNode json = (ObjectNode) mapper.readTree(jsonString);
//        json.remove("allAsList");
//        ArrayNode newJsonArrayNode = mapper.createArrayNode();
//        newJsonArrayNode.add(json);
//
//        return newJsonArrayNode;
//    }
}

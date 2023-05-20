package com.adem.elasticsearch.utility;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.spi.JsonProvider;

import java.io.Reader;
import java.io.StringReader;

public class EsJsonDataUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonData object2JsonData(Object object, ElasticsearchClient esClient) throws JsonProcessingException {
        String document = OBJECT_MAPPER.writeValueAsString(object);
        Reader reader = new StringReader(document);

        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();
        return JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
    }
}

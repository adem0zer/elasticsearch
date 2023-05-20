package com.adem.elasticsearch.service;

import java.io.IOException;

public interface EsService {
    String createIndex(String indexName) throws IOException;
    void deleteIndex(String indexName) throws IOException;
    boolean checkIndexExist(String indexName) throws IOException;
}

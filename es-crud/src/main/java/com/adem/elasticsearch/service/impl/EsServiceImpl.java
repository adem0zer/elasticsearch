package com.adem.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.adem.elasticsearch.service.EsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EsServiceImpl implements EsService {
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public String createIndex(String indexName) throws IOException {
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(cr -> cr.index(indexName));
        return createIndexResponse.index();
    }

    @Override
    public void deleteIndex(String indexName) throws IOException {
        elasticsearchClient.indices().delete(del -> del.index(indexName));
    }

    @Override
    public boolean checkIndexExist(String indexName) throws IOException {
        BooleanResponse exists = elasticsearchClient.indices().exists(ExistsRequest.of(ch -> ch.index(indexName)));
        return exists.value();
    }

    public boolean createObject() {

        return false;
    }
}

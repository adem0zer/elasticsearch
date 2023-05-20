package com.adem.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    // Create the low-level client
    @Bean
    RestClient restClient() {
        BasicCredentialsProvider creadential = new BasicCredentialsProvider();
        creadential.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials("elastic", "elastic")
        );

        return RestClient.builder(new HttpHost("localhost", 9200, "http"))
                .setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(10000) // time until a connection with the server is established.
                                .setSocketTimeout(60000) // time of inactivity to wait for packets[data] to receive.
                                .setConnectionRequestTimeout(0))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder
                                .setDefaultCredentialsProvider(creadential)
                                .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build())
                                )
                .build();
    }

    // Create the transport with a Jackson mapper
    @Bean
    ElasticsearchTransport transport() {
        return new RestClientTransport(
                restClient(), new JacksonJsonpMapper());
    }

    // And create the API client
    @Bean
    ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(transport());
    }
}

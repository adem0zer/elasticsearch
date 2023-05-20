package com.adem.elasticsearch.controller;

import com.adem.elasticsearch.service.EsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("es")
@RequiredArgsConstructor
public class ESController {

    private final EsService esService;

    @GetMapping(value = "{indexName}")
    public boolean checkIndexName(@PathVariable String indexName) throws IOException {
        return esService.checkIndexExist(indexName);
    }

    @PostMapping(value = "{indexName}")
    public String createIndex(@PathVariable String indexName) throws IOException {
        return esService.createIndex(indexName);
    }

    @DeleteMapping(value = "{indexName}")
    public void deleteIndex(@PathVariable String indexName) throws IOException {
        esService.deleteIndex(indexName);
    }
}

package com.george.item.elastic;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.*;

import java.io.IOException;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElasticTest {
    private RestHighLevelClient client;

    private static final String json = """
            {
              "mappings": {
                "properties": {
                  "id": {
                    "type": "keyword"
                  },
                  "name": {
                    "type": "text",
                    "analyzer": "ik_smart"
                  },
                  "price": {
                    "type": "integer"
                  },
                  "image": {
                    "type": "binary",
                    "index": false
                  },
                  "category": {
                    "type": "keyword"
                  },
                  "brand": {
                    "type": "keyword"
                  },
                  "sold": {
                    "type": "integer"
                  },
                  "comment_count": {
                    "type": "integer",
                    "index": false
                  },
                  "isAD": {
                    "type": "boolean"
                  },
                  "update_time": {
                    "type": "date"
                  }
                }
              }
            }""";

    @Test
    @Order(0)
    public void test() {
        System.out.println(client);
    }

    @Test
    @Order(2)
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("items");
        request.source(json, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    @Order(3)
    public void testGetIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("items");
        boolean isExist = client.indices().exists(request, RequestOptions.DEFAULT);
        log.info("{}", isExist);
    }

    @Test
    @Order(1)
    public void testDeleteIndex() {
        DeleteIndexRequest request = new DeleteIndexRequest("items");
        try {
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("删除失败:{}", e.getMessage());
        }
    }

    @BeforeEach
    public void before() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.68.128", 9200, "http")
        ));
    }

    @AfterEach
    public void after() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                log.info("{}", e.getMessage());
            }
        }
    }
}

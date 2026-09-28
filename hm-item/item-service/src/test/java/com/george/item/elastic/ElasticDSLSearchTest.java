package com.george.item.elastic;

import cn.hutool.json.JSONUtil;
import com.george.item.domain.po.ItemDoc;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@Slf4j
@SpringBootTest(properties = "spring.profiles.active=local")
public class ElasticDSLSearchTest {
    private RestHighLevelClient client;

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

    @Test
    public void testMatch() throws IOException {
        // 创建DSL的SearchRequest请求对象
        SearchRequest searchRequest = new SearchRequest("items");

        // 构造请求体
        searchRequest.source().query(QueryBuilders.matchQuery("name", "拉杆箱"));

        // 执行查询，返回SearchResponse响应对象
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        printHit(searchResponse);
    }


    @Test
    public void testBoolQuery() throws IOException {
        // 创建DSL的SearchRequest请求对象
        SearchRequest searchRequest = new SearchRequest("items");

        // 构造请求体
        searchRequest.source().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name", "脱脂牛奶"))
                        .filter(QueryBuilders.termQuery("brand", "德亚"))
                        .filter(QueryBuilders.rangeQuery("price").lte(10000))
        );

        // 执行查询，返回SearchResponse响应对象
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        printHit(searchResponse);
    }


    @Test
    public void testSortAndPage() throws IOException {
        // 创建DSL的SearchRequest请求对象
        SearchRequest searchRequest = new SearchRequest("items");

        // 构造请求体
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        // 分页查询，每次查询50条
        int pageNo = 1, pageSize = 50;
        searchRequest.source().from((pageNo - 1) * pageSize).size(pageSize);
        // 查询结果按倒序排序
        searchRequest.source().sort("price", SortOrder.DESC);

        // 执行查询，返回SearchResponse响应对象
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        printHit(searchResponse);
    }


    @Test
    public void testHighlight() throws IOException {
        // 创建DSL的SearchRequest请求对象
        SearchRequest searchRequest = new SearchRequest("items");

        // 构造请求体
        searchRequest.source().query(QueryBuilders.matchQuery("name", "脱脂牛奶"));
        searchRequest.source().highlighter(new HighlightBuilder()
                .field("name")
                .preTags("<em>")
                .postTags("</em>")
        );

        // 执行查询，返回SearchResponse响应对象
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();

        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> hfs = hit.getHighlightFields();
            HighlightField hf = hfs.get("name");
            log.info("{}", hf.getFragments()[0].toString());
        }
    }


    public void printHit(SearchResponse searchResponse) {
        SearchHits searchHits = searchResponse.getHits();

        Assertions.assertNotNull(searchHits.getTotalHits());
        long total = searchHits.getTotalHits().value;
        log.info("共查询到{}条记录", total);

        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            ItemDoc itemDoc = JSONUtil.toBean(json, ItemDoc.class);
            log.info("{}", itemDoc);
        }
    }
}

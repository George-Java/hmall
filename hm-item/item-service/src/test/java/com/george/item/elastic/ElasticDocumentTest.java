package com.george.item.elastic;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.george.item.domain.po.Item;
import com.george.item.domain.po.ItemDoc;
import com.george.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(properties = "spring.profiles.active=local")
public class ElasticDocumentTest {
    private RestHighLevelClient client;

    @Autowired
    private IItemService itemService;

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
    public void testCreateDocument() throws IOException {
        //从数据库查询商品信息
        Item item = itemService.getById(100002672298L);

        //包装为itemDoc对象
        ItemDoc itemDoc = BeanUtil.copyProperties(item, ItemDoc.class);

        //获取request
        IndexRequest request = new IndexRequest("items").id("100002672298");

        request.source(JSONUtil.toJsonStr(itemDoc), XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        log.info("{}", indexResponse.getIndex());
    }


    @Test
    public void testGetDocument() throws IOException {
        //获取request
        GetRequest request = new GetRequest("items", "100002672298");

        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        //获取查询结果中的source并输出
        /*{
            "_index" : "item",
            "_type" : "_doc",
            "_id" : "100002672298",
            "_version" : 3,
            "_seq_no" : 2,
            "_primary_term" : 1,
            "found" : true,
            "_source" : {
                "id" : "100002672298",
                "name" : "三星 Galaxy Note9（SM-N9600）【分期用6GB+128GB】 寒霜蓝 移动联通电信4G游戏手机 双卡双待",
                "price" : 44800,
                "image" : "https://m.360buyimg.com/mobilecms/s720x720_jfs/t27082/302/324013085/140782/145fdd/5b8e3b98N4c3dcd05.jpg!q70.jpg.webp",
                "category" : "手机",
                "brand" : "三星",
                "sold" : 0,
                "commentCount" : 0,
                "isAD" : false,
                "updateTime" : 1556640000000
        }
        }*/
        log.info("{}", response.getSourceAsString());
    }


    @Test
    //删除文档
    public void testDeleteDocument() throws IOException {
        //获取DeleteRequest对象
        DeleteRequest request = new DeleteRequest("items", "100002672298");

        client.delete(request, RequestOptions.DEFAULT);
    }


    @Test
    //测试更新文档
    public void testUpdateDocument() throws IOException {
        //获取request
        UpdateRequest request = new UpdateRequest("items", "100002672298");

        request.doc(
                "price", 25600,
                "comment", 243488
        );

        client.update(request, RequestOptions.DEFAULT);
    }


    @Test
    public void testBulkDocument() throws IOException {
        //获取BulkRequest对象
        BulkRequest request = new BulkRequest();

        List<Item> items = itemService.lambdaQuery().list();
        List<ItemDoc> itemDocs = new ArrayList<>();
        for (Item item : items) {
            itemDocs.add(BeanUtil.copyProperties(item, ItemDoc.class));
        }

        for (ItemDoc itemDoc : itemDocs) {
            request.add(new IndexRequest("items")
                    .id(itemDoc.getId())
                    .source(JSONUtil.toJsonStr(itemDoc), XContentType.JSON));
        }

        //发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }
}

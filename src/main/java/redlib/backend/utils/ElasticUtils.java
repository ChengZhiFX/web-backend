package redlib.backend.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import redlib.backend.dto.query.KeywordQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.LuceneResultBookVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ElasticUtils {
    public static String INDEX_NAME = "books";

    public static CreateIndexResponse createIndex(ElasticsearchClient client) {
        try {
            return client.indices().create(builder -> builder
                    .mappings(typeMappingBuilder -> typeMappingBuilder
                            .properties("book_id", propertyBuilder -> propertyBuilder.integer(keywordBuilder -> keywordBuilder))
                            .properties("content", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                    )
                    .index(INDEX_NAME));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static DeleteIndexResponse deleteIndex(ElasticsearchClient client) {
        try {
            return client.indices().delete(builder -> builder.index(INDEX_NAME));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BulkOperation addBulkBook(Integer bookId, String title) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("book_id", bookId);
        doc.put("content", title);
        return new BulkOperation.Builder().create(builder -> builder.index(INDEX_NAME).id(bookId.toString()).document(doc)).build();
    }

    public static BulkResponse bulkAddDocument(ElasticsearchClient client, List<BulkOperation> list) {
        try {
            return client.bulk(builder -> builder.index(INDEX_NAME).operations(list));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static DeleteResponse deleteDocument(ElasticsearchClient client, Integer id) {
        try {
            return client.delete(builder -> builder.index(INDEX_NAME).id(id.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UpdateResponse<Map> updateDocument(ElasticsearchClient client, Integer bookId, String content) {
        if (!StringUtils.hasText(content) || bookId == null) {
            return null;
        }

        Map<String, Object> doc = new HashMap<>();
        doc.put("book_id", bookId);
        doc.put("content", content);
        try {
            return client.update(builder -> builder.index(INDEX_NAME).id(bookId.toString()).doc(doc), Map.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Page<LuceneResultBookVO> query(ElasticsearchClient client, KeywordQueryDTO queryDTO) {
        SearchResponse<Map> response;
        try {
            response = client.search(searchRequestBuilder -> searchRequestBuilder
                            .index(INDEX_NAME)
                            .query(queryBuilder -> queryBuilder
                                    .match(matchQueryBuilder -> matchQueryBuilder
                                            .field("content").query(queryDTO.getKeyword())))

                            .highlight(highlightBuilder -> highlightBuilder
                                    .preTags("<span style='color: red;'>")
                                    .postTags("</span>")
                                    .fields("content", highlightFieldBuilder -> highlightFieldBuilder))
                            .from((queryDTO.getCurrent() - 1) * queryDTO.getPageSize())
                            .size(queryDTO.getPageSize())
                    , Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long total = response.hits().total().value();
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), (int) total);
        List<LuceneResultBookVO> voList = new ArrayList<>();
        List<Hit<Map>> hits = response.hits().hits();
        for (Hit<Map> hit : hits) {
            Map product = hit.source();
            LuceneResultBookVO vo = new LuceneResultBookVO();
            vo.setBookId((Integer) product.get("book_id"));
            vo.setScore(hit.score());

            Map<String, List<String>> map = hit.highlight();
            vo.setHighlights(map.get("content"));
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }
}

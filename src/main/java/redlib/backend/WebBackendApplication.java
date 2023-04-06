package redlib.backend;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redlib.backend.utils.ElasticUtils;

@SpringBootApplication
@MapperScan("redlib.backend.dao")
@Slf4j
public class WebBackendApplication implements CommandLineRunner {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public static void main(String[] args) {
        SpringApplication.run(WebBackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            ElasticUtils.createIndex(elasticsearchClient);
        } catch (Exception ex) {
        }
    }
}

package redlib.backend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConfig {
    public final String username = "elastic";
    public final String password = "g40hAO_Li5+8VtrhM74u";

    public final String hostname = "localhost";

    public final Integer port = 9200;    // 默认端口号

    public final String scheme = "http";  // 协议

    @Bean
    public RestClient getRestClient() {
        // 创建用户名密码认证
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json; compatible-with=8")};
        // Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/x-www-form-urlencoded")};

        // 使用 low level rest client 进行连接和认证
        return RestClient.builder(
                        new HttpHost(hostname, port, scheme)
                ).setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(
                                credentialsProvider)
                ).setDefaultHeaders(defaultHeaders)
                .build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        // 使用 transport 加载 json 的对象映射器
        return new RestClientTransport(getRestClient(),
                new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        // 创建操作用的 ElasticsearchClient 对象
        return new ElasticsearchClient(getElasticsearchTransport());
    }
}

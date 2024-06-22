package com.zero.triptalk.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

import java.util.Base64;

@Slf4j
@Configuration
public class ElasticSearchConfig {

    private static final String BASIC = "Basic ";
    private static final String AUTH = "Authorization";

    @Value("${spring.elasticsearch.hostname}")
    private String hostname;
    @Value("${spring.elasticsearch.port}")
    private Integer port;
    @Value("${spring.elasticsearch.username}")
    private String userName;
    @Value("${spring.elasticsearch.password}")
    private String password;

    @Bean
    public RestClient restClient() {

        return RestClient.builder(new HttpHost(hostname, port))
                .setDefaultHeaders(new BasicHeader[]{
                        new BasicHeader(AUTH,
                                BASIC + Base64.getEncoder()
                                        .encodeToString((userName + password).getBytes()))})
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {

        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper();
        jsonpMapper.objectMapper().registerModule(new JavaTimeModule())
                                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ElasticsearchTransport transport = new RestClientTransport(this.restClient(), jsonpMapper);

        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(ElasticsearchClient elasticsearchClient, ElasticsearchConverter elasticsearchConverter) {

        return new ElasticsearchTemplate(elasticsearchClient, elasticsearchConverter);
    }

}

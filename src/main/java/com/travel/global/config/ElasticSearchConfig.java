package com.travel.global.config;

//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.travel.domain.accommodation.repository")
//public class ElasticSearchConfig {
//
//    @Value("${spring.data.elasticsearch.username}")
//    String username;
//
//    @Value("${spring.data.elasticsearch.password}")
//    String password;
//
//    @Value("${spring.data.elasticsearch.host}")
//    String host;
//
//    @Value("${spring.data.elasticsearch.port}")
//    int port;
//
//    @Value("${spring.data.elasticsearch.fingerprint}")
//    String fingerprint;
//
//
//    @Bean
//    public RestClient restClient() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(
//            AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//
//        SSLContext sslContext = TransportUtils
//            .sslContextFromCaFingerprint(fingerprint);
//
//        return RestClient.builder(new HttpHost(host, port, "https"))
//            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
//                .setSSLContext(sslContext)
//                .setDefaultCredentialsProvider(credentialsProvider)
//                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE))
//            .build();
//    }
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
//        RestClientTransport transport = new RestClientTransport(restClient,
//            new JacksonJsonpMapper());
//        return new ElasticsearchClient(transport);
//    }
//}

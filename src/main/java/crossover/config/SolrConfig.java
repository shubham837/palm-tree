package crossover.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.HttpSolrClientFactoryBean;

@Configuration
public class SolrConfig {
    public static final String SOLR_HOST = "solr.server.host";
    public static final String SOLR_PORT = "solr.server.port";
    private static final Logger log = LoggerFactory.getLogger(SolrConfig.class);

    @Autowired
    private Environment env;

    private String solrHost;
    private int solrPort;

    @Bean
    public HttpSolrClientFactoryBean solrClientFactoryBean() {
        HttpSolrClientFactoryBean factory = new HttpSolrClientFactoryBean();
        factory.setUrl(this.getSolrUrl());
        return factory;
    }

    @Bean
    public SolrClient solrClient() {
        return solrClientFactoryBean().getSolrClient();
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        solrHost = env.getProperty(SOLR_HOST, "localhost");
        solrPort = Integer.parseInt(env.getProperty(SOLR_PORT, "8983"));
        log.info("Solr Host: " + solrHost + " Solr Port: " + solrPort);
        if (solrHost == null) {
            return null;
        }
        return new SolrTemplate(solrClientFactoryBean().getObject());
    }

    protected String getSolrUrl() {
        if(solrHost == null) {
            solrHost = env.getProperty(SOLR_HOST, "localhost");
            solrPort = Integer.parseInt(env.getProperty(SOLR_PORT, "8983"));
        }
        return String.format("http://%s:%d/solr/crossover", solrHost, solrPort);
    }

}
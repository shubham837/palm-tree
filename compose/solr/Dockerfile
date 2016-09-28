FROM solr:6.1.0

COPY compose/solr/solr-entrypoint.sh /usr/local/bin/
COPY compose/solr/managed-schema /tmp/
COPY crossover_data.csv /tmp/
USER root
RUN  chmod +x /usr/local/bin/solr-entrypoint.sh
USER solr
ENTRYPOINT ["solr-entrypoint.sh"]

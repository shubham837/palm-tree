FROM cassandra:2.2.7
RUN mkdir -p /tmp
COPY compose/cassandra/cassandra-entrypoint.sh /usr/local/bin/
COPY compose/cassandra/database_creation.cql /tmp/
USER root
#RUN  apt-get update \
#  && apt-get install -y wget \
#  && rm -rf /var/lib/apt/lists/*

RUN  chmod +x /usr/local/bin/cassandra-entrypoint.sh
USER cassandra

ENTRYPOINT ["/usr/local/bin/cassandra-entrypoint.sh", "-e", "CASSANDRA_BROADCAST_ADDRESS=${CASSANDRA_BROADCAST_ADDRESS}"]
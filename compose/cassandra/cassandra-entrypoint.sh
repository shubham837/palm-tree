#!/bin/bash
set -e

echo "cassandra config: " $CASSANDRA_CONFIG
echo "Starting Cassandra Database with broadcast address: " ${CASSANDRA_BROADCAST_ADDRESS}

sed -i -e "s/^rpc_address.*/rpc_address: 0.0.0.0/" $CASSANDRA_CONFIG/cassandra.yaml
sed -i -e "s/^.*broadcast_address.*/broadcast_address: ${CASSANDRA_BROADCAST_ADDRESS}/" $CASSANDRA_CONFIG/cassandra.yaml
sed -i -e "s/^.*broadcast_rpc_address.*/broadcast_rpc_address: ${CASSANDRA_BROADCAST_ADDRESS}/" $CASSANDRA_CONFIG/cassandra.yaml
sed -i -e "s/- seeds:.*/- seeds: ${CASSANDRA_BROADCAST_ADDRESS}/" $CASSANDRA_CONFIG/cassandra.yaml

# With virtual nodes disabled, we need to manually specify the token
echo "JVM_OPTS=\"\$JVM_OPTS -Dcassandra.initial_token=0\"" >> $CASSANDRA_CONFIG/cassandra-env.sh

# Pointless in one-node cluster, saves about 5 sec waiting time
echo "JVM_OPTS=\"\$JVM_OPTS -Dcassandra.skip_wait_for_gossip_to_settle=0\"" >> $CASSANDRA_CONFIG/cassandra-env.sh

# Most likely not needed
echo "JVM_OPTS=\"\$JVM_OPTS -Djava.rmi.server.hostname=$IP\"" >> $CASSANDRA_CONFIG/cassandra-env.sh

# If configured in $CASSANDRA_DC, set the cassandra datacenter.
if [ ! -z "$CASSANDRA_DC" ]; then
    sed -i -e "s/endpoint_snitch: SimpleSnitch/endpoint_snitch: PropertyFileSnitch/" $CASSANDRA_CONFIG/cassandra.yaml
    echo "default=$CASSANDRA_DC:rac1" > $CASSANDRA_CONFIG/cassandra-topology.properties
fi

exec cassandra -f
sleep 30

OUTPUT=/tmp/myscript.out
#echo "logging to $OUTPUT"
#{
#until $(wget -O - http://localhost:9042 | grep -q -i cassandra); do
#  echo "cassandra is not running yet"
#  sleep 5
#done
echo "cassandra is running"

#cqlsh 192.168.99.100 -f /tmp/database_creation.cql
#} </dev/null >$OUTPUT 2>&1 &
tail -f /dev/null
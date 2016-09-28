#!/bin/bash
COLLECTION="crossover"
echo "Starting solr server"

/opt/solr/bin/solr start

echo "$COLLECTION"

if [[ -d "/opt/solr/server/solr/$COLLECTION" ]]; then
  echo "$COLLECTION is already present on disk"
  exit 0
fi

OUTPUT=/opt/solr/myscript.out
echo "starting $0; logging to $OUTPUT"
{
until $(wget -O - http://localhost:8983 | grep -q -i solr); do
  echo "solr is not running yet"
  sleep 5
done
echo "solr is running"
if wget -O - 'http://localhost:8983/solr/admin/cores' | grep $COLLECTION; then
  echo "$COLLECTION is already present"
  exit 0
fi
echo creating $COLLECTION core
/opt/solr/bin/solr create_core -c $COLLECTION
echo "created $COLLECTION core"

echo "updating schema"
mv /tmp/managed-schema /opt/solr/server/solr/crossover/conf/

echo "Reloading updated schema on Solr Server"

/opt/solr/bin/solr restart
sleep 10

echo "Indexing existing data"
java -Dtype=text/csv -Durl=http://localhost:8983/solr/crossover/update -jar ~/opt/packages/solr-6.1.0/example/exampledocs/post.jar  /tmp/crossover_data.csv
} </dev/null >$OUTPUT 2>&1 &
tail -f /dev/null
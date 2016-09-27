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
echo created $COLLECTION core
} </dev/null >$OUTPUT 2>&1 &
tail -f /dev/null
#!/bin/bash


#curl http://localhost:8983/solr/db/dataimport?command=$1

# start solr server
bin/solr start

# create core for crossover with command
bin/solr create -c crossover -d basic_configs

# Now edit the managed-schema/schema.xml file in the \server\solr\jcg\conf folder and add the following contents after the uniqueKey element.

    <field name="type" type="text_general" indexed="true" stored="true" required="false" multiValued="false" />
    <field name="city" type="text_general" indexed="true" stored="true" required="false" multiValued="false" />
    <field name="province" type="text_general" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="country" type="text_general" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="zipCode" type="text_general" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="hasAirCondition" type="boolean" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="hasGarden" type="boolean" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="hasPool" type="boolean" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="isCloseToBeach" type="boolean" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="dailyPrice" type="double" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="currency" type="text_general" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="roomsNumber" type="double" indexed="true" stored="true" required="false" multiValued="true" />
    <field name="last_modified_by" type="text_general" indexed="true" stored="true" required="false" multiValued="true" />

# restart solr server
bin/solr stop
bin/solr start


# import existing data from csv to solr
java -Dtype=text/csv -Durl=http://localhost:8983/solr/crossover/update -jar ~/opt/packages/solr-6.1.0/example/exampledocs/post.jar  crossover_data.csv
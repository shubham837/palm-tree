CREATE KEYSPACE IF NOT EXISTS crossover WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'}  AND durable_writes = true;

CREATE TABLE IF NOT EXISTS crossover.cr_user (

    id uuid,

    name text,

    email text,

    created_ts timestamp,

    updated_ts timestamp,

    PRIMARY KEY(id, email)

)WITH CLUSTERING ORDER BY (email DESC);


CREATE TABLE IF NOT EXISTS crossover.cr_rental_info (

    id text,

    type text,

    city text,

    province text,

    country text,

    zip_code text,

    has_air_condition boolean,

    has_garden boolean,

    has_pool boolean,

    is_close_to_beach boolean,

    daily_price double,

    currency text,

    rooms_number double,

    created_ts timestamp,

    updated_ts timestamp,

    last_modified_by text,

    PRIMARY KEY (id, type)

) WITH CLUSTERING ORDER BY (type DESC);

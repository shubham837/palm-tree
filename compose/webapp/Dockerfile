FROM maven:latest

RUN mkdir -p /opt/app/crossover

WORKDIR /opt/app/crossover

COPY . /opt/app/crossover/

RUN chmod +x /opt/app/crossover/compose/webapp/entrypoint.sh

RUN mkdir -p /var/log/crossover/

ENTRYPOINT ["/opt/app/crossover/compose/webapp/entrypoint.sh"]

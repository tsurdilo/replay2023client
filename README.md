# Temporal Replay 2023 Demo

# start worker
mvn clean install
docker-compose up --build --detach

# scale to 2 workers
docker-compose up -d --build --scale replaydemo-worker=2


# actuator metrics (sdk metrics)

http://localhost:19998/actuator/prometheus

# prom targets
http://localhost:9090/targets?search=
# grafana 
http://localhost:8085/

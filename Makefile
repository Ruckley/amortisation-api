all: create_jar create_containers
create_jar:
	mvn clean install

create_containers:
	docker-compose up







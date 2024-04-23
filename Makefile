all: create_jar
create_jar:
	mvn clean install

create_contianers:
	docker-compose up







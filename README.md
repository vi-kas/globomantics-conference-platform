# globomantics-conference-platform
Repository for Pluralsight course - Scala Design Patterns
The repository contains codebase for different modules from Pluralsight course - Scala Design Patterns

The course is divided in multiple modules. For each module, code is available in a branch named module-x. E.g. module-4

To run project specific to a particular module:

Checkout the respective branch:
> git checkout module-3

Give `sbt run` to run the service.
Above command should run the service at localhost:8080.  You may use any REST Client e.g. POSTMAN to test the service endpoints.

E.g. To create a new User, you may send a POST request to `http://localhost:8080/user with below request JSON (Content-type: application/json).
```json
{
    "data": {
        "address": {
            "addressLine": "12-54, Block 428",
            "location": {
                "city": "Alexen",
                "country": "Alexa",
                "pin": "302012"
            }
        },
        "email": "alex_foo@mail.com",
        "id": "123c78ec-1eb8-4410-b284-f75079b15708",
        "name": "Alex Foo",
        "password": "plain_pwd",
        "role": "Speaker"
    },
    "success": true
}
```

Above request should give you a success response as `201 Created`.

Note: The project (module_4 onwards) uses Docker for running postgres database. So to follow along, ensure Docker is installed on your machine and running.

Once Docker is running:

Create a directory for docker volume e.g. On Mac/Linux:
> mkdir -p $HOME/docker/volumes/postgres

Run the postgres image
> docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres

Ensure if it's running:
> docker ps

The command should show something like:
```bash
7bc71fbb33e0 postgres "docker-entrypoint.s…" 14 minutes ago Up 14 minutes 0.0.0.0:5432->5432/tcp pg-docker
```

Configure postgres by following below steps:

Enter inside interactive bash shell on the container to use postgres
> docker exec -it pg-docker /bin/bash

Open psql for localhost
> psql -h localhost -U postgres -d postgres

> psql -h localhost -U trustworthy -d pgconfedb

Above command says Open psql server running on `localhost` under user `postgres` and connect to database named `postgres`.

Create a database, a user, and provide permissions on database to the user created.
```sql
CREATE DATABASE "pgconfedb" WITH ENCODING 'UTF8';
CREATE USER "trustworthy" WITH PASSWORD 'trust';
GRANT ALL ON DATABASE "pgconfedb" TO trustworthy;
```

This should set postgres with desired configuration. Above configuration(i.e. databaseName, user and password) is required in project's resources/application.conf file:
```conf
conferencedb = {
  connectionPool = "HikariCP"
  dataSourceClass = "org.postgresql.ds.PGSimpleDataSource"
  properties = {
	  serverName = "localhost"
	  portNumber = "5432"
	  databaseName = "pgconfedb"
	  user = "trustworthy"
	  password = "trust"
	  url = "jdbc:postgresql://localhost:5432/pgconfedb"
  }
  numThreads = 10
}
```
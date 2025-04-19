# README 

## objectives
* start.spring.io: Docker Compose, PostgreSQL, Spring Data JDBC, Batch
* Docker compose lifecycle: `start-only`
* we need some SQL data: `data.sql` and `schema.sql` 
* use `JdbcClient` to create a SQL repository to read  `person` and `dog` table data
* use Spring Data JDBC to create a SQL repository to read  `person` and `dog` table data
* create a service and use `@Transactional` to envelope interactions in transactions
* that's fine for day 1. but what about schema migrations? FlywayDB!
* with FlywayDB, we now have an empty SQL DB. How would I read it in en-masse?
* Create a Spring Batch `Job` to map `dogs.csv` data into `DogRecord` objects with an `int` for the `person` pointer.
* `@EventListener(JobExecutionEvent.class)` to see the results printed out

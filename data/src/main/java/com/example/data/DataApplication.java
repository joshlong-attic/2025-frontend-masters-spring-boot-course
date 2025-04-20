package com.example.data;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class DataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	@Bean
	FlatFileItemReader<DogRow> flatFileItemReader(@Value("classpath:/dogs.csv") Resource resource) {
		return new FlatFileItemReaderBuilder<DogRow>().resource(resource)
			.name("csvFlatFileItemReader")
			.linesToSkip(1)
			.delimited()//
			.names("person,id,name,description,dob,owner,gender,image".split(","))
			.fieldSetMapper(fieldSet -> new DogRow(fieldSet.readInt("id"), fieldSet.readInt("person"),
					fieldSet.readString("name"), fieldSet.readString("description")))
			.build();
	}

	@Bean
	JdbcBatchItemWriter<DogRow> jdbcBatchItemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<DogRow>().dataSource(dataSource)
			.assertUpdates(false)
			.sql("insert into dog (id, person, name, description) values(?,?,?,?) on conflict (id) do nothing")
			.itemPreparedStatementSetter((item, ps) -> {
				ps.setInt(1, item.id());
				if (item.person() > 0) {
					ps.setInt(2, item.person());
				} //
				else {
					ps.setNull(2, java.sql.Types.INTEGER);
				}
				ps.setString(3, item.name());
				ps.setString(4, item.description());
			})
			.build();
	}

	@Bean
	Step start(JobRepository repository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<DogRow> reader, JdbcBatchItemWriter<DogRow> writer) {
		return new StepBuilder("startStep", repository).<DogRow, DogRow>chunk(10, transactionManager)
			.reader(reader)
			.writer(writer)
			.build();
	}

	@Bean
	Job job(JobRepository repository, Step start) {
		return new JobBuilder("csvToDbJob", repository).incrementer(new RunIdIncrementer()).start(start).build();
	}

	record DogRow(int id, int person, String name, String description) {
	}

}

@Component
class JobListener {

	private final JdbcClientPersonRepository jdbc;

	private final PersonRepository data;

	JobListener(JdbcClientPersonRepository jdbc, PersonRepository data) {
		this.jdbc = jdbc;
		this.data = data;
	}

	@EventListener
	void afterBatchJobRun(JobExecutionEvent event) {
		System.out.println("finished the batch job " + event.getJobExecution().getJobInstance().getJobName());
		System.out.println("===================================");
		data.findAll().forEach(System.out::println);
		System.out.println("===================================");
		jdbc.findAll().forEach(System.out::println);
	}

}

record Person(@Id int id, String name, Set<Dog> dogs) {
}

record Dog(@Id int id, String name, String description) {
}

@Repository
class JdbcClientPersonRepository {

	private final JdbcClient db;

	JdbcClientPersonRepository(JdbcClient db) {
		this.db = db;
	}

	List<Person> findAll() {
		var mapping = new HashMap<Integer, Person>();
		return db.sql("""
				    select p.id as pid, p.name  as pName , d.id as did ,d.description as dDescription, d.name as dName ,
				           d.person as dPerson  from person p left join dog d on p.id = d.person
				""").query((rs, _) -> {
			var personId = rs.getInt("pid");
			var personName = rs.getString("pName");
			var dogId = rs.getInt("did");
			var dogName = rs.getString("dName");
			var dogDescription = rs.getString("dDescription");
			var person = mapping.computeIfAbsent(rs.getInt("pid"),
					_ -> new Person(personId, personName, new HashSet<>()));
			person.dogs().add(new Dog(dogId, dogName, dogDescription));
			return person;
		}).list();
	}

}

interface PersonRepository extends ListCrudRepository<Person, Integer> {

}

package in.laxmi.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import in.laxmi.entity.Customer;
import in.laxmi.repo.CustomerRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private CustomerRepository repository;
	@Autowired
    private StepBuilderFactory stepFactory;
	@Autowired
    private JobBuilderFactory jobFactory;
//item reader bean
	@Bean
	public FlatFileItemReader<Customer> customerReader() {
		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<Customer>();
		itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
		itemReader.setName("customer-item-read");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<Customer>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<Customer>();
		fieldSetMapper.setTargetType(Customer.class);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(lineTokenizer);
		return lineMapper;
	}

//item processor bean
	@Bean
	public CustomerProcesser customerProcessor() {
		return new CustomerProcesser();
	}

//item writer bean
	@Bean
	public RepositoryItemWriter<Customer> customerWritter() {
		RepositoryItemWriter<Customer> itemWritter = new RepositoryItemWriter<Customer>();
		itemWritter.setRepository(repository);
		itemWritter.setMethodName("save");
		return itemWritter;
	}

//step bean
	@Bean
	public Step step() {
     return stepFactory.get("step-1").<Customer,Customer>chunk(10)
    		 .reader(customerReader())
    		 .processor(customerProcessor())
    		 .writer(customerWritter())
    		 .build();
	}
//job bean
	@Bean
	public Job job() {
		return jobFactory.get("customer-import")
				.flow(step())
				.end()
				.build();
	}
}

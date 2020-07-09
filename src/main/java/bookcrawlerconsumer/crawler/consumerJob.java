//package bookcrawlerconsumer.crawler;
//
//import java.util.List;
//import java.util.Properties;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.batch.item.kafka.KafkaItemReader;
//import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import bookcrawlerconsumer.common.message.ScrapingMessage;
//import bookcrawlerconsumer.job.SimpleJobConfiguration;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class consumerJob {
//
//	private final JobBuilderFactory jobBuilderFactory;
//	private final StepBuilderFactory stepBuilderFactory;
//	private final KafkaProperties properties;
//	
//	private static final int chunkSize = 10;
//	
//
//	@Bean
//	KafkaItemReader<String, String> kafkaItemReader() {
//		Properties props = new Properties();
//		props.putAll(this.properties.buildConsumerProperties());
//		return new KafkaItemReaderBuilder<String, String>().partitions(0).consumerProperties(props)
//				.name("customer-reader").saveState(true).topic("customer-topic").build();
//	}
//
//	@Bean
//	public Job kafkajob() {
//		return jobBuilderFactory.get("kafkajob").incrementer(new RunIdIncrementer()).start(jpaItemWriterStep()).build();
//	}
//	
//	@Bean
//	public Step jpaItemWriterStep() {
//		return stepBuilderFactory.get("jpaItemWriterStep")
//				.chunk(chunkSize)
//				.reader(kafkaItemReader())
//				.processor(scraping())
//				.
//	}
//	
//
//	private JpaItemProcessor<ScrapingMessage, Scraping> scraping() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Bean
//	Step start() {
//		
//		ItemWriter writer = new ItemWriter<String>() {
//
//			@Override
//			public void write(List<? extends String> items) throws Exception {
//				items.forEach(item -> System.out.println("new customer: " + item));
//			}
//		};
//
//		return stepBuilderFactory.get("kafkajob").chunk(0).reader(kafkaItemReader()).writer(writer).build();
//	}
//}

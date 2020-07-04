package bookcrawlerconsumer.job;

import java.util.Random;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step scopeStep2() {
		return stepBuilderFactory.get("scopeStep2")
				.tasklet(scopeStep2Tasklet(null))
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestDate]") String requestDate) {
		return (contribution, chunkContext) -> {
            log.info(">>>>> this is scopeStep1");
            log.info(">>>>> requestDate = {}" , requestDate);
            return RepeatStatus.FINISHED;
        };
	}
	
	@Bean
	public Job scopeJob() {
		return jobBuilderFactory.get("simpleJob").start(scopeStep1(null))
				.build();
	}
	
	@Bean
	@JobScope
	public Step scopeStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
		
		return stepBuilderFactory.get("scopeStep1")
				.tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> this is scopeStep1");
                    log.info(">>>>> requestDate = {}" , requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
	}
	
	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
				.start(startStep())
				.next(decider())
				.from(decider())
					.on("ODD")
					.to(oddStep())
				.from(decider())
					.on("EVEN")
					.to(evenStep())
				.end()
				.build();
	}
	
	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep").tasklet((contribution, chunkContext) -> {
			log.info(">>>>> 홀수입니다.");
			return RepeatStatus.FINISHED;
		}).build();
	}

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> 짝수입니다.");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    
	@Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }
	
	public static class OddDecider implements JobExecutionDecider {

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random rand = new Random();

            int randomNumber = rand.nextInt(50) + 1;
            log.info("랜덤숫자: {}", randomNumber);

            if(randomNumber % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
	
	@Bean
	public Job stepNextConditionJob() {
		return jobBuilderFactory.get("stepNextConditionJob")
				.start(simpleStep1())
					.on("FAILED")
					.to(simpleStep3())
					.on("*")
					.end()
				.from(simpleStep1())
					.on("*")
					.to(simpleStep2())
					.next(simpleStep3())
					.on("*")
					.end()
				.end()
				.build();
	}
	
	@Bean
	public Job stepNextJob() {
		return jobBuilderFactory.get("stepNextJob")
				.start(simpleStep1())
				.next(simpleStep2())
				.next(simpleStep3())
				.build();
	}
	
	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob").start(simpleStep1()).build();
	}
	
	public Step simpleStep1() {
		return stepBuilderFactory.get("simpleStep1")
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is step1");
					contribution.setExitStatus(ExitStatus.FAILED);
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	public Step startStep() {
		return stepBuilderFactory.get("startStep")
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is startStep");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	public Step simpleStep2() {
		return stepBuilderFactory.get("simpleStep2")
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is step2");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	public Step simpleStep3() {
		return stepBuilderFactory.get("simpleStep3")
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is step3");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
}

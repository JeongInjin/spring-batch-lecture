package io.spring.batch.springbatch.lecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// --job.name=batchJob name=user1 date=20230101
@Configuration
@RequiredArgsConstructor
public class Limit_AllowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job BatchJob() {
        return this.jobBuilderFactory.get("batchJob")
                .start(step1())
                .next(step2())
                .build();
    }

    private Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("contribution = " + contribution + ", chunkContext = " + chunkContext);
                    return RepeatStatus.FINISHED;
                }))
                .allowStartIfComplete(true)
                .build();
    }

    private Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("contribution = " + contribution + ", chunkContext = " + chunkContext);
                    throw new RuntimeException("step2 was failed");
//                    return RepeatStatus.FINISHED;
                }))
                .startLimit(3)
                .build();
    }
}
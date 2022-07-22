package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExampleJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    @StepScope
    public PoiItemReader excelReader() {
        PoiItemReader reader = new PoiItemReader();
        reader.setResource(new FileSystemResource("./test.xlsx"));
        System.out.println("test ");
        reader.setRowMapper(rowMapper());
        return reader;
    }
    @Bean
    @StepScope
    public ItemWriter testWriter(){
        ItemWriter writer = items -> System.out.println("test");
        return writer;
    }

    @Bean
    public RowMapper rowMapper() {
        return new RowMapper() {
            @Override
            public Object mapRow(RowSet rs) throws Exception {
                System.out.println("rs.getCurrentRow()[0] = " + rs.getCurrentRow()[0]);
                return rs;
            }
        };
    }

    @Bean
    public Job ExampleJob(){
        Job exampleJob = jobBuilderFactory.get("exampleJob")
            .start(step())
            .build();
        return exampleJob;
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
            .chunk(1)
            .reader(excelReader())
            .writer(testWriter())
            .build();
    }
}

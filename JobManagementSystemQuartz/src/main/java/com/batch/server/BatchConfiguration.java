/**
 *
 */
package com.batch.server;

import javax.sql.DataSource;

/**
 * @author hjadhav1
 *
 */
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.batch.dao.BatchDao;
import com.batch.dao.BatchDaoImpl;
import com.batch.items.CustomAlertProcessor;
import com.batch.items.FileDeletingTasklet;
import com.batch.items.UpdateAlertAgeTasklet;
import com.batch.model.pojo.Alerts;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Value("file:C:\\Projects\\SpringBatchProjects\\Ownrepo\\jobManagementSystem\\JobManagementSystemQuartz\\src\\main\\resources\\input\\inputData.csv")
    private Resource inputResource;

    @Bean
    public FlatFileItemReader<Alerts> reader() {
        FlatFileItemReader<Alerts> itemReader = new FlatFileItemReader<Alerts>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setStrict(false);
        System.out.println("inside Reader job");
        itemReader.setResource(inputResource);
        return itemReader;
    }

    @Bean
    public LineMapper<Alerts> lineMapper() {
        DefaultLineMapper<Alerts> lineMapper = new DefaultLineMapper<Alerts>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"alertId", "description", "alertOwner", "relatedCaseId"});
        lineTokenizer.setIncludedFields(new int[]{0, 1, 2, 3});
        BeanWrapperFieldSetMapper<Alerts> fieldSetMapper = new BeanWrapperFieldSetMapper<Alerts>();
        fieldSetMapper.setTargetType(Alerts.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JdbcBatchItemWriter<Alerts> writer() {
        JdbcBatchItemWriter<Alerts> itemWriter = new JdbcBatchItemWriter<Alerts>();
        itemWriter.setDataSource(dataSource);
        System.out.println("inside writer job");
        System.out.println("Datasource :" + dataSource);
        itemWriter.setSql("INSERT INTO ALERTS (alertId, description, alertOwner,relatedCaseId,alertAge) VALUES (:alertId, :description, :alertOwner,:relatedCaseId,:alertAge)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Alerts>());
        return itemWriter;
    }

    @Bean
    public ItemProcessor<Alerts, Alerts> processor() {
        return new CustomAlertProcessor();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Alerts, Alerts>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Step step2() {
        FileDeletingTasklet task = new FileDeletingTasklet();
        task.setResource(inputResource);
        return stepBuilderFactory.get("step2").tasklet(task).build();

    }

    @Bean
    public Job moveCSVtoDb() {
        return jobBuilderFactory.get("moveCSVtoDb")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public BatchDao batchDaoObj() {
        BatchDaoImpl daoImpl = new BatchDaoImpl();
        daoImpl.setDataSource(dataSource);
        return daoImpl;
    }

    @Bean
    public Step updateAlertAgeStep() {
        UpdateAlertAgeTasklet task = new UpdateAlertAgeTasklet(batchDaoObj());
        return stepBuilderFactory.get("updateAlertAgeStep").tasklet(task).build();
    }

    @Bean
    public Job updateAlertAge() {
        return jobBuilderFactory.get("updateAlertAge")
                .incrementer(new RunIdIncrementer())
                .start(updateAlertAgeStep())
                .build();
    }
}

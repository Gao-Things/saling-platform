package com.usyd.capstone.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    private final SsmClient ssmClient;

    public DataSourceConfig() {
        // 初始化 SSM 客户端
        this.ssmClient = SsmClient.builder()
                .region(Region.of("us-east-1")) // 替换为你的区域
                .build();
    }

    private String getParameter(String parameterName) {
        // 从 SSM 获取参数
        GetParameterRequest parameterRequest = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true)
                .build();
        GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
        return parameterResponse.parameter().value();
    }

    @Bean
    public DataSource dataSource() {
        // 通过SSM获取参数
        String dbHost = getParameter("/capstone/production/db/host");
        String username = getParameter("/capstone/production/db/username");
        String password = getParameter("/capstone/production/db/password");

        // 构建完整的JDBC URL
        String dbUrl = "jdbc:mysql://" + dbHost + "/capstone?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 设置HikariCP连接池参数
        dataSource.setMaximumPoolSize(150);
        dataSource.setMinimumIdle(10);

        return dataSource;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // 从AWS Parameter Store获取用户名和密码
        mailSender.setUsername(getParameter("/capstone/production/email/username"));
        mailSender.setPassword(getParameter("/capstone/production/email/password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    @Bean
    public ConnectionFactory connectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(getParameter("/capstone/production/rabbitmq/host"));
        connectionFactory.setPort(5671); // SSL端口
        connectionFactory.setUsername(getParameter("/capstone/production/rabbitmq/username"));
        connectionFactory.setPassword(getParameter("/capstone/production/rabbitmq/password"));
        connectionFactory.setVirtualHost("/"); // 如果有特定的虚拟主机
        // 启用SSL支持
        connectionFactory.getRabbitConnectionFactory().useSslProtocol();

        return connectionFactory;
    }

    @Bean
    public S3Client s3Client() {
        String accessKeyId = getParameter("/capstone/production/s3/accessKeyId");
        String secretAccessKey = getParameter("/capstone/production/s3/secretAccessKey");

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of("us-east-1"))
                .build();
    }

    @Bean
    public String s3Bucket() {
        String s3BucketName = getParameter("/capstone/production/s3/bucketname"); // 参数名应与在AWS Parameter Store中的名称匹配
        return s3BucketName;
    }


    @Bean
    public String adminWebUrl() {
        String adminWebUrl = getParameter("/capstone/production/web/url"); // 参数名应与在AWS Parameter Store中的名称匹配
        return adminWebUrl;
    }


}

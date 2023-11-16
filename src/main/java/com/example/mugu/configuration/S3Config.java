package com.example.mugu.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}")
    private String iamAccessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String iamSecretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    //@Value("${cloud.aws.bucket.static}")
    private String bucketName = "mugu-bucket";
    @Bean
    public AmazonS3Client amazonS3Client(){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region).enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }



    public void upload(InputStream inputStream, String fileName) {
        AmazonS3Client s3Client = amazonS3Client();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, new ObjectMetadata()));
    }

    public String getUrl(String fileName) {
        AmazonS3Client s3Client = amazonS3Client();
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public void delete(String fileName) {
        AmazonS3Client s3client = amazonS3Client();
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }




}

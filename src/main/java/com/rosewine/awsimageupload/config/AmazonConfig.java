package com.rosewine.awsimageupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Indicates to Spring to use this as the configuration to connect to our AWS account via our (1)access-key (2)secret-key
public class AmazonConfig {

    /**
     * Return a S3 client to the caller.
     * @return
     */
    @Bean   // This annotation required so we can INJECT in calling classes.
    public AmazonS3 s3() {
        // Right now it is taking our access-key and secret-key as hard coded values:
        // OBVIOUSLY, this cannot be published to Github in this way!
        //AWSCredentials awsCredentials = new BasicAWSCredentials(
        //        "____________________", //accessKey
        //        "________________________________________"  //secretKey
        //);

        AccessKeys accessKeys = new AccessKeys("C:/temp/accessKeys.csv");
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                accessKeys.getAccessKey(),
                accessKeys.getSecretKey()
        );

        // We are now using the "builder"pattern to create the return value:
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)  // aw: I added this because I was getting a Region related exception. I wish now I had kept my S3 bucket in Sydney, but - too much stuffing around.
                .build();
    }
}

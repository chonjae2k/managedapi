package com.gscdn.managedapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigRequest;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigResult;

@Service
public class AwsApiService {
	@Value("${aws.accesskey}")
    private String accesskey;
    
    @Value("${aws.secretkey}")
    private String secretKey;
    
	
    public String invalidation( ) {
    	
    	
		
    	return "";
    }
    
    public GetDistributionConfigResult getDistributionConfig( ) {
    	
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials(accesskey, secretKey);
    	AmazonCloudFront amazonCloudFront = AmazonCloudFrontClientBuilder.standard()
    			.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
    	
		GetDistributionConfigRequest getDistributionConfigRequest = new GetDistributionConfigRequest();
		getDistributionConfigRequest.setId("E1FL50LZUX5QP0");	
		GetDistributionConfigResult getDistributionConfigResult = amazonCloudFront.getDistributionConfig(getDistributionConfigRequest);
		
    	return getDistributionConfigResult;
    }
}

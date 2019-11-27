package com.gscdn.managedapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigRequest;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigResult;
import com.gscdn.managedapi.model.gscdn.CheckTicketRequest;

@Service
public class GsCdnApiService {

	RestTemplate restTemplate = new RestTemplate();

	public String getTicketResult() {

		CheckTicketRequest chk = new CheckTicketRequest("cloud", "e7bc30542d-56a876e5haop", "QT000000000000012144");

		HttpHeaders headers = new HttpHeaders();
		

		HttpEntity<CheckTicketRequest> requestGs = new HttpEntity<>(chk, headers);

		ResponseEntity<String> result = restTemplate.postForEntity("https://stagecdnapi.gscdn.com/api/checkTicket",requestGs, String.class);
		
		return result.getBody();
	}
}

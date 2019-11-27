package com.gscdn.managedapi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import org.apache.tomcat.util.buf.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.cloudfront.model.CacheBehavior;
import com.amazonaws.services.cloudfront.model.CacheBehaviors;
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest;
import com.amazonaws.services.cloudfront.model.CreateInvalidationResult;
import com.amazonaws.services.cloudfront.model.DistributionConfig;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigRequest;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigResult;
import com.amazonaws.services.cloudfront.model.InvalidationBatch;
import com.amazonaws.services.cloudfront.model.Paths;
import com.gscdn.managedapi.model.auth.Token;
import com.gscdn.managedapi.model.entity.User;
import com.gscdn.managedapi.model.purge.PurgeRequest;


import org.joda.time.format.DateTimeFormat;
@RunWith(SpringRunner.class)
@RestClientTest(RestTest.class)
public class RestTest {
	RestTemplate restTemplate = new RestTemplate();
	
	
	//@Test
	public void test() {
		System.out.println("test");
		System.out.println(restTemplate.getForObject("http://127.0.0.1:8080/v1/users", User.class, 25));
		String[] paths = {"12","123"};
		PurgeRequest purgeRequest = new PurgeRequest("DI001",paths);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("accesskey", "1");
		headers.set("signature", "2");
		
		HttpEntity<PurgeRequest> request = new HttpEntity<>(purgeRequest, headers);
		
		ResponseEntity<String> result = restTemplate.postForEntity("http://127.0.0.1:8080/v1/purge", request, String.class);
		System.out.println(result);
	}
	
	//@Test
	public void testGSCDN() {
		System.out.println("testGSCDN");
		/*System.out.println(restTemplate.getForObject("http://127.0.0.1:8080/v1/users", User.class, 25));
		String[] paths = {"12","123"};*/
		CheckTiket chk = new CheckTiket("cloud", "e7bc30542d-56a876e5haop", "QT000000000000012144");
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("accesskey", "1");
		headers.set("signature", "2");
		
		HttpEntity<CheckTiket> request = new HttpEntity<>(chk, headers);
		
		ResponseEntity<String> result = restTemplate.postForEntity("https://stagecdnapi.gscdn.com/api/checkTicket", request, String.class);
		System.out.println(result);
		
		
	}
	
	//@Test
	//Signature v4
	public void testCF() throws Exception {
		System.out.println("testCF");
		
		String method = "GET";
		String service = "cloudfront";
		String host = "cloudfront.amazonaws.com";
		String region = "us-east-1";
		String endpoint = "https://cloudfront.amazonaws.com";
		String request_parameters = "";
		
		String access_key = "AKIAJVHZ6PB252ES44UQ";
		String secret_key = "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";
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat formatter2= new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		SimpleDateFormat formatter3= new SimpleDateFormat("yyyyMMdd");
    	Date date = new Date(System.currentTimeMillis());
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
    	formatter3.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println(formatter.format(date));
    	System.out.println(formatter2.format(date));
    	System.out.println(formatter3.format(date));
		
    	String amzdate = formatter2.format(date);
    	String dateStamp = formatter3.format(date);
    	
    	String canonical_uri = "/2019-03-26/distribution/E1FL50LZUX5QP0/config";
    	String canonical_querystring = request_parameters;
    	String canonical_headers = "host:" + host + "\n" + "x-amz-date:" + amzdate + "\n";
    	String signed_headers = "host;x-amz-date";
    	
    	String payload_hash = HexUtils.toHexString(sha256(""));
    	String canonical_request = method + '\n' + canonical_uri + '\n' + canonical_querystring + '\n' + canonical_headers + '\n' + signed_headers + '\n' + payload_hash;
    	
    	System.out.println("canonical_request = " + canonical_request);
    	
    	
    	String algorithm = "AWS4-HMAC-SHA256";
    	String credential_scope = dateStamp + '/' + region + '/' + service + '/' + "aws4_request";
    	String string_to_sign = algorithm + '\n' +  amzdate + '\n' +  credential_scope + '\n' + HexUtils.toHexString(sha256(canonical_request));
    	
    	System.out.println("string_to_sign = " + string_to_sign);
    	byte[] signing_key = getSignatureKey(secret_key, dateStamp, region, service);
    	
    	byte[] signatureByte = HmacSHA256(string_to_sign, signing_key);
    	String signature = HexUtils.toHexString(signatureByte);
        //System.out.println("Signature : " + HexUtils.toHexString(signature));
        //System.out.println("Signature2 : " + bytesToHex1(signature));
        
        String authorization_header = algorithm + ' ' + "Credential=" + access_key + '/' + credential_scope + ", " +  "SignedHeaders=" + signed_headers + ", " + "Signature=" + signature;
		//String authorization = "AWS4-HMAC-SHA256 Credential=AKIAJVHZ6PB252ES44UQ/20191125/us-east-1/cloudfront/aws4_request, SignedHeaders=host;x-amz-date, Signature=";
		//String auth = authorization+HexUtils.toHexString(signature);
		//String auth = authorization + "861e47616471465aaddbd79761c80bda85ab09b37aeddef1c744df38d64c0ae0";
		System.out.println(authorization_header);
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-amz-date", amzdate);
		headers.set("Authorization", authorization_header);
		headers.set("Content-Type", "application/json");
		System.out.println(headers);
		HttpEntity entity = new HttpEntity(headers);
		
		//ResponseEntity<String> result = restTemplate.getForEntity("https://cloudfront.amazonaws.com/2019-03-26/distribution/E1FL50LZUX5QP0/config", headers, String.class);
		ResponseEntity<DistributionConfig> result = restTemplate.exchange("https://cloudfront.amazonaws.com/2019-03-26/distribution/E1FL50LZUX5QP0/config", HttpMethod.GET, entity,DistributionConfig.class);
		System.out.println(result);
		System.out.println(result.getBody().getCallerReference());
		
		
		
		
	}
	
	//@Test
	public void testCFBySdk() {
		System.out.println("testCFSDK");
		
		String method = "GET";
		String service = "cloudfront";
		String host = "cloudfront.amazonaws.com";
		String region = "us-east-1";
		String endpoint = "https://cloudfront.amazonaws.com";
		String request_parameters = "";
		
		String access_key = "AKIAJVHZ6PB252ES44UQ";
		String secret_key = "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";
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJVHZ6PB252ES44UQ", "ZYuFtWcti0lf9hFd4donm7CXGJp28649YUYu4ZAa");
		
		AmazonCloudFront amazonCloudFront = AmazonCloudFrontClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
		
		
		GetDistributionConfigRequest getDistributionConfigRequest = new GetDistributionConfigRequest();
		//getDistributionConfigRequest.setId("E1AIOXGKY58AA6");
		getDistributionConfigRequest.setId("E1FL50LZUX5QP0");	
		GetDistributionConfigResult getDistributionConfigResult = amazonCloudFront.getDistributionConfig(getDistributionConfigRequest);
		
		CreateInvalidationRequest createInvalidationRequest = new CreateInvalidationRequest();
		//set distrudition id
		createInvalidationRequest.setDistributionId("E1FL50LZUX5QP0");
		//set paths
		Paths paths = new Paths().withItems("*/*").withQuantity(1);
		createInvalidationRequest.setInvalidationBatch(new InvalidationBatch().withPaths(paths));
		CreateInvalidationResult createInvalidationResult = amazonCloudFront.createInvalidation(createInvalidationRequest);
		System.out.println(createInvalidationResult);
	}
	//@Test
	public void authTest() {
		System.out.println("testAUTH");
		/*System.out.println(restTemplate.getForObject("http://127.0.0.1:8080/v1/users", User.class, 25));
		String[] paths = {"12","123"};*/
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
		HttpEntity entity = new HttpEntity(headers);
		//HttpEntity<CheckTiket> request = new HttpEntity<>(chk, headers);
		Map<String, String> params = new HashMap<String, String>();
		String endPoint = "https://auth.gsclip.com:9090/oauth/token";
		String grant = "password";
		String username = "demo@gscdn.com";
		String password = "SPdhxpr1!";
		params.put("grant_type", "password");
		params.put("username", "demo@gscdn.com");
		params.put("password", "SPdhxpr1!");
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(params, headers);
		String url = endPoint + "?grant_type=" + grant + "&username=" + username + "&password=" + password;
		ResponseEntity<Token> result = restTemplate.postForEntity(url, entity, Token.class);
		System.out.println(result);
		System.out.println(result.getBody().getAccess_token());
	}
	@Test
	public void tokenTest() {
		System.out.println("testToken");
		/*System.out.println(restTemplate.getForObject("http://127.0.0.1:8080/v1/users", User.class, 25));
		String[] paths = {"12","123"};*/
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
		//headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
		HttpEntity entity = new HttpEntity(headers);
		//HttpEntity<CheckTiket> request = new HttpEntity<>(chk, headers);
		Map<String, String> params = new HashMap<String, String>();
		String endPoint = "https://clipper-restapi-client:callthemanager@auth.gsclip.com:9090/oauth/check_token";
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJDdXN0VHlwZSI6IjEiLCJsZXZlbCI6bnVsbCwidXNlcl9uYW1lIjoiZGVtb0Bnc2Nkbi5jb20iLCJDdXN0Tm0iOiLtgbTrnbzsmrDrk5xf6rCc67CcIiwiQ3VzdG9tZXJJRCI6IjAzMDciLCJFTWFpbCI6ImRlbW9AZ3NjZG4uY29tIiwiTW9iaWxlIjoiMDEwLTAwMDAtMDAwMCIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiZW1wc3RhdGUiOiIxIiwiY2xpZW50X2lkIjoiY2xpcHBlci1yZXN0YXBpLWNsaWVudCIsImF1ZCI6WyJzcGFya2xyIl0sIkN1c3RJRCI6IjAzMDciLCJzY29wZSI6WyJiaWxsaW5nOnN0YXRpc3RpY3MiLCJiaWxsaW5nOmFjY291bnRzIl0sIkVtcE5NIjoi642w66qo6rOg6rCdIiwicGVybWl0IjoiMSIsIk1hc3RlckFjdCI6ImRlbW9AZ3NjZG4uY29tIiwiT3duQWNjb3VudHMiOm51bGwsIkRlcHROYW1lIjoi6rCc67Cc67aAIiwiZXhwIjoxNTc0NzU5NjYyLCJqdGkiOiIzMzI5MTFhMy04N2I5LTRlYjYtOTEyMC02ODdiNWI1YmMyODMifQ.iw234fLc0wDb3ivMJgD9bBxH7F3fEn6mnXfMt3M_CnmJzaZUA7I3xydKFCPpeBjXiRsH37SgisHeenCB70xM4X3TAm-X-O84TNyyBjAo4oV3GqZwls4Csj4He9IeBpS4Y-98C4f43cxD-mZFnYZwvcz6SrbRxfoZ6z2B9J-65Fcjdoh4grV1Z23YX-nJTljsp6o8eDM5LhrJj3G458xuj9HuWBykuVK_QqedoDrOKTFih3sa_APkn1wOUcJuVRzfq5493EjlQss8YnpPCLjzGFtlni7TMV6wCi9RSsoxk6g8rMcPaPIthSCZhneXLQhmI4nuf6WCSw-5-jPPp7HVIQ";
		String url = endPoint + "?token=" + token;
		System.out.println(url);
		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
		System.out.println(result);
		//System.out.println(result.getBody().getAccess_token());
	}
	public class CheckTiket {
		String accessId;
		String AccessKey;
		String ticketId;
		public CheckTiket(String accessId, String accessKey, String ticketId) {
			super();
			this.accessId = accessId;
			AccessKey = accessKey;
			this.ticketId = ticketId;
		}
		public String getAccessId() {
			return accessId;
		}
		public void setAccessId(String accessId) {
			this.accessId = accessId;
		}
		public String getAccessKey() {
			return AccessKey;
		}
		public void setAccessKey(String accessKey) {
			AccessKey = accessKey;
		}
		public String getTicketId() {
			return ticketId;
		}
		public void setTicketId(String ticketId) {
			this.ticketId = ticketId;
		}
		
		
		
		
	}
	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return mac.doFinal(data.getBytes("UTF-8"));
	}

	static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
	    byte[] kSecret = ("AWS4" + key).getBytes("UTF-8");
	    byte[] kDate = HmacSHA256(dateStamp, kSecret);
	    byte[] kRegion = HmacSHA256(regionName, kDate);
	    byte[] kService = HmacSHA256(serviceName, kRegion);
	    byte[] kSigning = HmacSHA256("aws4_request", kService);
	    return kSigning;
	}
	public static byte[] sha256(String msg) throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(msg.getBytes());
	    
	    return md.digest();
	}
	
	public static String bytesToHex1(byte[] bytes) {
	    StringBuilder builder = new StringBuilder();
	    for (byte b: bytes) {
	      builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}

	
}


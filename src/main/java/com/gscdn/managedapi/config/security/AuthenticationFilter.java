package com.gscdn.managedapi.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import com.google.gson.Gson;
import com.gscdn.managedapi.advice.exception.CUserNotFoundException;
import com.gscdn.managedapi.model.response.CommonResult;
import com.gscdn.managedapi.model.response.SingleResult;
import com.gscdn.managedapi.repo.DistributionJpaRepo;
import com.gscdn.managedapi.service.ResponseService;

import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/v1/purge")
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {
	
	/*private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList("", "/login", "/logout", "/register")));*/
	private FilterConfig mFilterConfig = null;
	private final ResponseService responseService;
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		boolean checkFlag = false;
		String token = "";
		if(servletRequest instanceof HttpServletRequest){
            String url = ((HttpServletRequest) servletRequest).getRequestURI().toString();
            String queryString  = ((HttpServletRequest) servletRequest).getQueryString();
            token = ((HttpServletRequest) servletRequest).getHeader("AUTH-TOKEN");
            System.out.println("url::"+url);
            System.out.println("url::"+queryString);
            System.out.println("AUTH-TOKEN = " + token);
        }
		//if(token != "") {
			try {
				System.out.println("Do filter");
		        System.out.println("testToken");
				/*System.out.println(restTemplate.getForObject("http://127.0.0.1:8080/v1/users", User.class, 25));
				String[] paths = {"12","123"};*/
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "application/json");
				headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
				//headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
				HttpEntity entity = new HttpEntity(headers);
				//HttpEntity<CheckTiket> request = new HttpEntity<>(chk, headers);
				Map<String, String> params = new HashMap<String, String>();
				String endPoint = "https://clipper-restapi-client:callthemanager@auth.gsclip.com:9090/oauth/check_token";
				String url = endPoint + "?token=" + token;
				System.out.println(url);
				ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
				System.out.println(result);
				int resCode = result.getStatusCodeValue();
	            if (resCode == 200) {
	            	checkFlag = true;
	            }
			}catch(Exception e) {
				servletResponse.setCharacterEncoding("UTF-8"); 
				servletResponse.setContentType("text/html; charset=UTF-8"); 
				PrintWriter out =servletResponse.getWriter();
				CommonResult comm = new CommonResult();
				comm.setCode(-1);
				comm.setMsg("토큰이 유효하지 않습니다");
				comm.setSucess(false);
			
				Gson gson = new Gson();
				String outtext = gson.toJson(comm);
				System.out.println(outtext);
				out.print(outtext);
				out.flush();
				return;
			}
	            
		//}
		/*if (checkFlag == false) {
			System.out.println("Error: Token Error");
			servletRequest.setAttribute("auth", "fail");
			throw new CUserNotFoundException();
		}	*/
		
		
	    filterChain.doFilter(servletRequest,servletResponse);
		
        
		
		
	}
	
	public CommonResult responseInvalidation(HttpServletResponse response) {
		
		return responseService.getFailResult();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		Filter.super.destroy();
		System.out.println("destroy filter");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		Filter.super.init(filterConfig);
		mFilterConfig = filterConfig;
		System.out.println("init filter");
	}


	
}

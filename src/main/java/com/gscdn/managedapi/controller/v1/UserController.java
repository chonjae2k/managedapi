package com.gscdn.managedapi.controller.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gscdn.managedapi.advice.exception.CUserNotFoundException;
import com.gscdn.managedapi.model.auth.Token;
import com.gscdn.managedapi.model.entity.User;
import com.gscdn.managedapi.model.response.CommonResult;
import com.gscdn.managedapi.model.response.ListResult;
import com.gscdn.managedapi.model.response.SingleResult;
import com.gscdn.managedapi.repo.UserJpaRepo;
import com.gscdn.managedapi.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = { "1. Admin" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

	private final UserJpaRepo userJpaRepo;
	private final ResponseService responseService; // 결과를 처리할 Service
	
	
	@ApiOperation(value = "로그인", notes = "이메일 로그인을 한다.")
	@GetMapping(value = "/signin")
	public SingleResult<Token> signin(@ApiParam(value = "ID : 이메일", required = true) @RequestParam String id,
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
		System.out.println("testAUTH");
		/*
		 * System.out.println(restTemplate.getForObject(
		 * "http://127.0.0.1:8080/v1/users", User.class, 25)); String[] paths =
		 * {"12","123"};
		 */
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		headers.set("Authorization", "Basic Y2xpcHBlci1yZXN0YXBpLWNsaWVudDpjYWxsdGhlbWFuYWdlcg==");
		HttpEntity entity = new HttpEntity(headers);
		// HttpEntity<CheckTiket> request = new HttpEntity<>(chk, headers);
		Map<String, String> params = new HashMap<String, String>();
		String endPoint = "https://auth.gsclip.com:9090/oauth/token";
		String grant = "password";
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(params, headers);
		String url = endPoint + "?grant_type=" + grant + "&username=" + id + "&password=" + password;
		ResponseEntity<Token> result = restTemplate.postForEntity(url, entity, Token.class);
		System.out.println(result);
		System.out.println(result.getBody().getAccess_token());

		return responseService.getSingleResult(result.getBody());
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
	@GetMapping(value = "/users")
	public ListResult<User> findAllUser() {
		return responseService.getListResult(userJpaRepo.findAll());
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
	@PutMapping(value = "/user")
	public SingleResult<User> modify(@ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name) {
		User user = User.builder().id(msrl).name(name).build();
		return responseService.getSingleResult(userJpaRepo.save(user));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 삭제", notes = "회원번호(msrl)로 회원정보를 삭제한다")
	@DeleteMapping(value = "/user/{msrl}")
	public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {
		userJpaRepo.deleteById(msrl);
		return responseService.getSuccessResult();
	}
}

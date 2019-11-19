package com.gscdn.managedapi.controller.v1;

import java.util.List;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gscdn.managedapi.model.entity.User;
import com.gscdn.managedapi.model.response.CommonResult;
import com.gscdn.managedapi.model.response.ListResult;
import com.gscdn.managedapi.model.response.SingleResult;
import com.gscdn.managedapi.repo.UserJpaRepo;
import com.gscdn.managedapi.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = {"1. User"})
@RestController
@RequestMapping(value = "/v1")

public class UserController {
	@Autowired
	private UserJpaRepo userJpaRepo;
	@Autowired
	private ResponseService responseService;
	
	@ApiOperation(value = "회원전체조회", notes = "모든 회원 조회")
	@GetMapping(value = "/user")
    public ListResult<User> findAllUser() {
		return responseService.getListResult(userJpaRepo.findAll());
        //return userJpaRepo.findAll();
    }
	
	@ApiOperation(value = "회원 단건 조회", notes = "id로 회원조회")
	@GetMapping(value = "/user/{id}")
	public SingleResult<User> findUserById(@ApiParam(value = "회원Id", required=true) @PathVariable int id,@RequestBody String json) throws Exception {
		System.out.println(json);
		return responseService.getSingleResult(userJpaRepo.findById(id).orElseThrow(Exception::new));
	}
 
	@ApiOperation(value = "회원 입력", notes = "회원 등록")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원아이디", required = true) @RequestParam int id,
    		@ApiParam(value = "회원 이름", required=true) @RequestParam String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        //user.setCompany("wife");
        return responseService.getSingleResult(userJpaRepo.save(user));
        //return userJpaRepo.save(user);
    }
	
	@ApiOperation(value = "회원 수정", notes = "회원 정보를 수정한다")
	@PutMapping(value = "user")
	public SingleResult<User> modeify(
			@ApiParam(value = "회원ID", required = true) @RequestParam int id,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name,
			@ApiParam(value = "회원회사", required = true) @RequestParam String company){
		User user = new User();
		user.setCompany(company);
		user.setId(id);
		user.setName(name);
		return responseService.getSingleResult(userJpaRepo.save(user));
	}
	
	@ApiOperation(value = "회원 삭제", notes = "회원 정보를 삭제한다")
	@DeleteMapping(value = "user/{id}")
	public CommonResult delete(
			@ApiParam(value = "회원ID", required = true) @PathVariable int id){
		userJpaRepo.deleteById(id);
		
		return responseService.getSuccessResult();
	}
	
}

package com.gscdn.managedapi.controller.v1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
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

import com.gscdn.managedapi.advice.exception.CUserNotFoundException;
import com.gscdn.managedapi.model.purge.PurgeRequest;
import com.gscdn.managedapi.model.purge.PurgeResponse;
import com.gscdn.managedapi.model.entity.Distribution;
import com.gscdn.managedapi.model.entity.User;
import com.gscdn.managedapi.model.response.CommonResult;
import com.gscdn.managedapi.model.response.ListResult;
import com.gscdn.managedapi.model.response.SingleResult;
import com.gscdn.managedapi.repo.DistributionJpaRepo;
import com.gscdn.managedapi.repo.UserJpaRepo;
import com.gscdn.managedapi.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import net.minidev.json.parser.JSONParser;

@Api(tags = {"2. Purge"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class PurgeController {

	private final DistributionJpaRepo distributionJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service
    
    
    /*
	 * 1. purge 수행*
	 */
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "accesskey", value = "인증 구현(예정)", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "Signature", value = "인증 구현(예정)", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "purge 수행", notes = "Purge 기능을 수행한다")
    @PostMapping(value = "/purge")
    public SingleResult<PurgeResponse> prugeTest(@ApiParam(value = "json", required = true) @RequestBody PurgeRequest json) {
    	/*1. 요청한 데이터 기반으로 user 테이블을 조회해서 domainID 와 distribution Id 조회
    	2. CF 존재하면 invalidation 수행 후 Ticket 발급 
    	3. CDN 존재하면 Purge 수행 후 Ticket 발급
    	4. Ticket 정보를 ticket 테이블에 저장
    	*/
    	
    	//1. distrubitionId 조회
    	System.out.println(distributionJpaRepo.findByUid(json.getDid())); 
    	
    	//2,3 수행
    	
    	//4 수행
    	
    	//5 return
    	
    	String[] paths = json.getPaths();
    	SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date = new Date(System.currentTimeMillis());
    	System.out.println(formatter.format(date));
    	
    	PurgeResponse purgeResponse = PurgeResponse.builder().ticket("TK00000001").createTime(formatter.format(date)).paths(paths).build();
    	//User user = userJpaRepo.findById(id);
        return responseService.getSingleResult(purgeResponse);
    }
    
}



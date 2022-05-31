package com.kruten.jarsofttesttask.controller;

import com.kruten.jarsofttesttask.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/")
public class RequestController {

    @Autowired
    RequestService requestService;

    @GetMapping("bid")
    public ResponseEntity<?> getOnlyBanner(@RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent
            , @RequestParam(value = "cat") List<String> reqCategoryIdList
            , HttpServletRequest ipAddress){
        return requestService.getBannerWithCategories(userAgent, reqCategoryIdList, ipAddress);
    }
}

package com.kruten.jarsofttesttask.service;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.entity.Request;
import com.kruten.jarsofttesttask.repository.BannerRep;
import com.kruten.jarsofttesttask.repository.CategoryRep;
import com.kruten.jarsofttesttask.repository.RequestRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    RequestRep requestRep;

    @Autowired
    CategoryRep categoryRep;

    @Autowired
    BannerRep bannerRep;

    public ResponseEntity<?> getOnlyBanner(String userAgent, String reqName, HttpServletRequest ipAddress){
        String ip = ipAddress.getRemoteAddr();
        Category category = categoryRep.findCategoryByReqName(reqName);
        if (category == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<Banner> banners = new ArrayList<>();
        category.getBanners().stream().filter(banner -> !banner.isDeleted()).forEach(banners::add);
        banners.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));

        LocalDateTime currentDateTime = LocalDateTime.now();
        Banner banner;

        while (banners.size() > 0){
            banner = banners.get(0);
            Request request = requestRep.findFirstByUserAgentAndIpAddressAndBannerIdOrderByIdDesc(userAgent, ip, banner);

            if (request == null){
                requestRep.save(new Request(banner, userAgent, ip, currentDateTime));
                return new ResponseEntity<>(banner.getContent(), HttpStatus.OK);
            }
            else {
                LocalDateTime bannerTime = request.getDateTime();
                if (ChronoUnit.DAYS.between(currentDateTime, bannerTime) > 0){
                    requestRep.save(new Request(banner, userAgent, ip, currentDateTime));
                    return new ResponseEntity<>(banner.getContent(), HttpStatus.OK);
                }
                else banners.remove(0);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

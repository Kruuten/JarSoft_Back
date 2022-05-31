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
import java.util.*;

@Service
public class RequestService {
    @Autowired
    RequestRep requestRep;

    @Autowired
    CategoryRep categoryRep;

    @Autowired
    BannerRep bannerRep;

    public ResponseEntity<?> getBannerWithCategories(String userAgent, List<String> reqCategoryIDList, HttpServletRequest ipAddress) {
        List<Banner> banners = bannerRep.findAllByDeletedIsFalse();
        if (banners.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<Category> bannerCategories;
        List<Banner> bannersToShow = new ArrayList<>();
        for (int i = 0; i < banners.size(); i++) {
            bannerCategories = new ArrayList<>(banners.get(i).getCategories());
            if (bannerCategories.size() < reqCategoryIDList.size()) {
                banners.remove(banners.get(i));
            }

            List<String> bannerCategoriesReqId = new ArrayList<>();
            for (Category category : bannerCategories) {
                String id = category.getReqName();
                bannerCategoriesReqId.add(id);
            }


            if (new HashSet<>(bannerCategoriesReqId).containsAll(reqCategoryIDList))
                bannersToShow.add(banners.get(i));
        }

        bannersToShow.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));

        LocalDateTime currentDateTime = LocalDateTime.now();

        while (bannersToShow.size() > 0) {
            Banner responseBanner = bannersToShow.get(bannersToShow.size()-1);
            String ip  = ipAddress.getRemoteAddr();
            Request request = requestRep.findFirstByUserAgentAndIpAddressAndBannerIdOrderByIdDesc(userAgent, ip, responseBanner);

            if (request == null){
                requestRep.save(new Request(responseBanner, userAgent, ip, currentDateTime));
                return new ResponseEntity<>(responseBanner.getContent(), HttpStatus.OK);
            }
            else {
                LocalDateTime bannerTime = request.getDateTime();
                if (ChronoUnit.DAYS.between(currentDateTime, bannerTime) > 0){
                    requestRep.save(new Request(responseBanner, userAgent, ip, currentDateTime));
                    return new ResponseEntity<>(responseBanner.getContent(), HttpStatus.OK);
                }
                else bannersToShow.remove(bannersToShow.size()-1);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
}

package com.kruten.jarsofttesttask.controller;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @GetMapping("banners")
    public ResponseEntity<List<Banner>> getBanners(@RequestParam(required = false) String name) {
        try {
            return bannerService.getBanners(name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("banners/{id}")
    public ResponseEntity<Banner> getBanner(@PathVariable int id){
        try {
            return bannerService.getBanner(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("banners/{id}")
    public ResponseEntity<?> editBanner(@Valid @RequestBody  Banner bannerDetails, @PathVariable int id){
        try {
            return bannerService.editBanner(bannerDetails, id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("banners")
    public ResponseEntity<?> addNewBanner(@Valid @RequestBody Banner banner){
        try {
            return bannerService.createNewBanner(banner);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("banners/{id}")
    public ResponseEntity<Banner> deleteBanner(@PathVariable int id) {
        try {
            return bannerService.deleteBanner(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

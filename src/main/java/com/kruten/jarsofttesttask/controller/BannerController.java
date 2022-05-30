package com.kruten.jarsofttesttask.controller;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.security.security.jwt.JwtUtils;
import com.kruten.jarsofttesttask.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

//    @GetMapping("/banners")
//    public ResponseEntity<List<Banner>> getBanners() {
//        return bannerService.getBanners();
//    }

    @GetMapping("/banners")
    public ResponseEntity<List<Banner>> getBanners(@RequestParam(required = false) String name) {
        try {
            return bannerService.getBanners(name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/banners/{id}")
    public ResponseEntity<Banner> getBanner(@PathVariable int id){
        try {
            return bannerService.getBanner(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("banners/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editBanner(@RequestBody @Valid Banner bannerDetails, @PathVariable int id){
        try {
            return bannerService.editBanner(bannerDetails, id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/banners")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> addNewBanner(@RequestBody @Valid Banner banner){
        try {
            return bannerService.createNewBanner(banner);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/banners/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Banner> deleteBanner(@PathVariable int id) {
        try {
            return bannerService.deleteBanner(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

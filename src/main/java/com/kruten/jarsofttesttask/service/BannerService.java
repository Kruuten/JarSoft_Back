package com.kruten.jarsofttesttask.service;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.repository.BannerRep;
import com.kruten.jarsofttesttask.repository.CategoryRep;
import com.kruten.jarsofttesttask.validator.ErrorResponse;
import com.kruten.jarsofttesttask.validator.Violation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BannerService {
    @Autowired
    private BannerRep bannerRep;

    @Autowired
    CategoryRep categoryRep;

    public ResponseEntity<List<Banner>> getBanners(String name){
        try {
            List<Banner> banners = new ArrayList<>();

            if (name != null)
                bannerRep.findAllByNameContainingIgnoreCase(name)
                        .stream()
                        .filter(banner -> !banner.isDeleted())
                        .forEach(banners::add);

            else bannerRep.findAll()
                    .stream()
                    .filter(banner -> !banner.isDeleted())
                    .forEach(banners::add);

            if (banners.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(banners, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Banner> getBanner(int id){
        Optional<Banner> optional = bannerRep.findByIdAndDeletedIsFalse(id);
        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        } else return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> editBanner(Banner banner, int id){
        Optional<Banner> optional = bannerRep.findById(id);
        Set<Category> categories = banner.getCategories();
        String bannerExists = String.format("Banner [%s] already exist", banner.getName());
        if (optional.isPresent()){
            if (bannerRep.existsBannerByNameAndIdNotLike(banner.getName(), banner.getId())) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.getViolations().add(new Violation("name", bannerExists));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            else {
                for (Category category : categories) {
                    banner.addCategoryToBanner(category);
                    category.addBannerToCategory(banner);
                }
                Banner editBanner = optional.get();
                editBanner.setName(banner.getName());
                editBanner.setPrice(banner.getPrice());
                editBanner.setCategories(banner.getCategories());
                editBanner.setContent(banner.getContent());
                return new ResponseEntity<>(bannerRep.save(editBanner), HttpStatus.OK);
            }
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> createNewBanner(Banner banner){
        Set<Category> categories = banner.getCategories();
        String bannerExists = String.format("Banner [%s] already exist", banner.getName());

        try{
            if (bannerRep.existsBannerByName(banner.getName())) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.getViolations().add(new Violation("name", bannerExists));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            else {
                for (Category category: categories) {
                    banner.addCategoryToBanner(category);
                    category.addBannerToCategory(banner);
                }
                return new ResponseEntity<>(bannerRep.save(
                        new Banner(banner.getName()
                                , banner.getPrice()
                                , banner.getCategories()
                                , banner.getContent()))
                        , HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Banner> deleteBanner(int id){
        Optional<Banner> optional = bannerRep.findById(id);

        if (optional.isPresent()){
            Banner banner = optional.get();
            banner.setDeleted(true);
            return new ResponseEntity<>(bannerRep.save(banner), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

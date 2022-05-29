package com.kruten.jarsofttesttask.service;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.repository.BannerRep;
import com.kruten.jarsofttesttask.repository.CategoryRep;
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
        if (bannerRep.findById(id).isPresent())
            return new ResponseEntity<>(this.bannerRep.findById(id).get(), HttpStatus.OK);
        else return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Banner> editBanner(Banner bannerDetails, int id){
        Optional<Banner> optional = bannerRep.findById(id);
        Set<Category> categories = bannerDetails.getCategories();
//        Category category = categoryRep.findCategoryByName(bannerDetails.getCategory().getName());
        if (optional.isPresent()){
            if (bannerRep.existsBannerByNameAndIdNotLike(bannerDetails.getName(), bannerDetails.getId()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else {
                for (Category category : categories) {
                    bannerDetails.addCategoryToBanner(category);
                    category.addBannerToCategory(bannerDetails);
                }
                Banner banner = optional.get();
                banner.setName(bannerDetails.getName());
                banner.setPrice(bannerDetails.getPrice());
                banner.setCategories(bannerDetails.getCategories());
                banner.setContent(bannerDetails.getContent());
                return new ResponseEntity<>(bannerRep.save(banner), HttpStatus.OK);
            }
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Banner> createNewBanner(Banner banner){
        Set<Category> categories = banner.getCategories();
//        Category category = categoryRep.findCategoryByName(banner.getCategory().getName());

        try{
            if (bannerRep.existsBannerByName(banner.getName()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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

package com.kruten.jarsofttesttask.repository;

import com.kruten.jarsofttesttask.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRep extends JpaRepository<Banner, Integer> {
    List<Banner> findAllByNameContainingIgnoreCase(String name);
    boolean existsBannerByName(String name);
    boolean existsBannerByNameAndIdNotLike (String name, int id);

}

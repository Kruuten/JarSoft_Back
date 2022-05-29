package com.kruten.jarsofttesttask.repository;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRep extends JpaRepository<Request, Integer> {
    Request findFirstByUserAgentAndIpAddressAndBannerIdOrderByIdDesc(String userAgent, String ip, Banner banner);
}

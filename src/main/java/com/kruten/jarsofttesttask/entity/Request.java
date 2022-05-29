package com.kruten.jarsofttesttask.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Setter
@Getter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BANNER_ID")
    private Banner bannerId;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "DATE")
    private LocalDateTime dateTime;

    public Request() {
    }

    public Request(Banner bannerId, String userAgent, String ipAddress, LocalDateTime dateTime) {
        this.bannerId = bannerId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.dateTime = dateTime;
    }
}

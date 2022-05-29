package com.kruten.jarsofttesttask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    @NotBlank(message = "Category name could not be empty")
    @Length(max = 255, message = "Name length should be < 255 characters")
    private String name;

    @Column(name = "REQ_NAME")
    @NotBlank(message = "Req. name could not be empty")
    @Length(max = 255, message = "Req. name length should be < 255 characters")
    private String reqName;

    @Column(name = "DELETED")
    private boolean deleted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "banner_category",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "BANNER_ID"))
    private Set<Banner> banners;

    public void addBannerToCategory(Banner banner) {
        if (banners == null) {
            banners = new HashSet<>();
        }
        banners.add(banner);
    }

    public Category(String name, String reqName) {
        this.name = name;
        this.reqName = reqName;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", reqName='" + reqName + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}

package com.kruten.jarsofttesttask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kruten.jarsofttesttask.validator.annotations.Price;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Name could not be empty")
    @Length(max = 255, message = "Name length should be < 255 characters")
    private String name;

    @Column(name = "PRICE")
    @NotNull
    @Positive(message = "Price should be >0")
    @Price(message = "price should be between 0.01 and 99999.99")
    private float price;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "banner_category",
            joinColumns = @JoinColumn(name = "BANNER_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private Set<Category> categories = new HashSet<>();

    @Column(name = "CONTENT")
    @NotBlank(message = "Content should be chosen")
    private String content;

    @Column(name = "DELETED")
    private boolean deleted;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "bannerId")
    private List<Request> requests;

    public void addCategoryToBanner(Category category) {
        if (categories == null) {
            categories = new HashSet<>();
        }
        categories.add(category);
    }

    public Banner() {
    }

    public Banner(String name, float price, Set<Category> categories, String content) {
        this.name = name;
        this.price = price;
        this.content = content;
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", requests=" + requests +
                '}';
    }
}

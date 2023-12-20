package com.example.TheSecondHandWardrobe.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "advertisement")
@Table
public class Advertisement {

    @Id
    @SequenceGenerator(name = "advertisement_sequence", sequenceName = "advertisement_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advertisement_sequence")
    private Long id;
    private String details;
    private String defects;

    private String model;
    private String size;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    public Advertisement(String details, String defects, String model, String size, AppUser appUser) {
        this.details = details;
        this.defects = defects;
        this.model = model;
        this.size = size;
        this.appUser = appUser;
    }


}

package com.example.TheSecondHandWardrobe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "image")
@Table
public class Image {

    @Id
    @SequenceGenerator(name = "image_sequence", sequenceName = "image_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence")
    private Long id;

    @Column(length = 100000000)
    private String base;

    @Lob
    private byte[] imageBytes;

    @OneToOne(mappedBy = "image")
    private Advertisement advertisement;

    public Image(String base) {
        this.base = base;
    }
}

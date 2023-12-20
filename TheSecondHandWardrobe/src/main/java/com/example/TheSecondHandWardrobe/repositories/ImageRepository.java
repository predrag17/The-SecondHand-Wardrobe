package com.example.TheSecondHandWardrobe.repositories;

import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.entities.AppUser;
import com.example.TheSecondHandWardrobe.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByAdvertisement(Advertisement advertisement);
}

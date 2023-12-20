package com.example.TheSecondHandWardrobe.repositories;

import com.example.TheSecondHandWardrobe.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAllByModelIgnoreCase(String model);


}

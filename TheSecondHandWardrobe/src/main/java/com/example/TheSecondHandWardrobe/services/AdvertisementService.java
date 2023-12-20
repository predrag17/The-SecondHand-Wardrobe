package com.example.TheSecondHandWardrobe.services;

import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.repositories.AdvertisementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;


    public void addAd(Advertisement advertisement) {
        advertisementRepository.save(advertisement);
    }

    public List<Advertisement> printAds() {
        return advertisementRepository.findAll();
    }


    @Transactional
    public Advertisement findAd(Long id) {
        return advertisementRepository.getReferenceById(id);
    }

    @Transactional
    public List<Advertisement> printByModel(String model) {
        return advertisementRepository.findAllByModelIgnoreCase(model);
    }

    public void deleteAd(Long id) {
        advertisementRepository.deleteById(id);
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }
}

package com.example.TheSecondHandWardrobe.services;


import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.entities.AppUser;
import com.example.TheSecondHandWardrobe.entities.Image;
import com.example.TheSecondHandWardrobe.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;


@AllArgsConstructor
@Service
public class ImageService {

    private ImageRepository imageRepository;

    public Image addImage(MultipartFile file) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        Image image = new Image(base64Image);
        image.setImageBytes(file.getBytes());
        return imageRepository.save(image);
    }
//
//    public Image findByImageData(String base64) {
//        return imageRepository.findByBase(base64);
//    }

    @Transactional
    public Image findByAdvertisement(Advertisement advertisement) {
        return imageRepository.findByAdvertisement(advertisement);
    }
}

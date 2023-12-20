package com.example.TheSecondHandWardrobe.services;

import com.example.TheSecondHandWardrobe.entities.AppUser;
import com.example.TheSecondHandWardrobe.entities.ConfirmationToken;
import com.example.TheSecondHandWardrobe.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).get();
        if (confirmationToken == null) {
            return;
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}

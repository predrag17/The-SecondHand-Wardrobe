package com.example.TheSecondHandWardrobe.services;

import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.entities.AppUser;
import com.example.TheSecondHandWardrobe.entities.ConfirmationToken;
import com.example.TheSecondHandWardrobe.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }

    public ResponseEntity<Object> signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists) {
            AppUser userToRegister = appUserRepository.findByEmail(appUser.getEmail()).get();
            if (userToRegister.getEnabled()) {
                return ResponseEntity.badRequest().body("Error: Е-маил адресата е веќе зафатена");
            } else {
                return ResponseEntity.badRequest().body("Error: Профилот не е активиран. Потврдете на вашата е-маил адреса!");
            }

        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        System.out.println("Sign up user");
        return ResponseEntity.ok().body(token);
    }

    public void enableUser(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).get();

        appUser.setEnabled(true);
    }

    public AppUser findByEmail(String email) {
        boolean exists = appUserRepository.existsAppUserByEmail(email);
        if (exists) {
            return appUserRepository.findByEmail(email).get();
        } else {
            return null;
        }

    }

}

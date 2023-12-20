package com.example.TheSecondHandWardrobe.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String name;
    private final String lastName;
    private final String email;
    private final String password;
}

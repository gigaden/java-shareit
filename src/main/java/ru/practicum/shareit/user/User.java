package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


@Data
@Builder
public class User {
    private Long id;
    @NonNull
    private String name;
    @Email
    @NonNull
    private String email;
}

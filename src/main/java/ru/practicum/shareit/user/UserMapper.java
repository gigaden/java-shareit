package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder().id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

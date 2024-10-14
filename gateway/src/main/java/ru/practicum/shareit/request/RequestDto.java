package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    @NonNull
    @NotBlank
    private String description;
    private Long requestor;
}

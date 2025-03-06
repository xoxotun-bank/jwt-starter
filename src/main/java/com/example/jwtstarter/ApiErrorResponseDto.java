package com.example.jwtstarter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponseDto {

    private int code;

    private String message;

}

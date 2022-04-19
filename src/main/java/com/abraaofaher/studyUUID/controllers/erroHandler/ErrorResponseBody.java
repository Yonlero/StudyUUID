package com.abraaofaher.studyUUID.controllers.erroHandler;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseBody {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
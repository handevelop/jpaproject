package com.example.jpa.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResult {

    private boolean result;

    private String message;

    public static ServiceResult fail(String message) {
        return ServiceResult.builder ()
                .message(message)
                .result(false)
                .build ();
    }

    public static ServiceResult success(String message) {
        return ServiceResult.builder ()
                .message (message)
                .result(true)
                .build ();
    }

    public boolean isFail() {
        return !result;
    }
}

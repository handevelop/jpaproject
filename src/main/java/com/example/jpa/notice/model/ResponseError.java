package com.example.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseError {

    private String field;

    private String message;

    public static ResponseError of(FieldError e) {
        return ResponseError.builder ()
                .field (e.getField ())
                .message (e.getDefaultMessage ())
                .build ();
    }

    public static List<ResponseError> of(List<ObjectError> errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();
        if (errors != null) {
            errors.stream ().forEach ((e) -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
        }
        return responseErrorList;
    }
}

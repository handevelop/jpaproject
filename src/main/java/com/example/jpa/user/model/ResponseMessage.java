package com.example.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseMessage {

    private ResponseMessageHeader header;

    private Object body;

    public static ResponseMessage fail(String message){
        return ResponseMessage.builder ()
                .header(ResponseMessageHeader.builder ()
                        .message (message)
                        .result (false)
                        .resultCode ("")
                        .status (HttpStatus.BAD_REQUEST.value ())
                        .build ())
                .body(null)
                .build ();
    }

    public static ResponseMessage fail(String message, Object data){
        return ResponseMessage.builder ()
                .header(ResponseMessageHeader.builder ()
                        .message (message)
                        .result (false)
                        .resultCode ("")
                        .status (HttpStatus.BAD_REQUEST.value ())
                        .build ())
                .body(data)
                .build ();
    }

    public static ResponseMessage success(Object data) {
        return ResponseMessage.builder ()
                .header(ResponseMessageHeader.builder ()
                        .message ("")
                        .result (true)
                        .resultCode ("")
                        .status (HttpStatus.OK.value ())
                        .build ())
                .body(data)
                .build ();
    }

    public static ResponseMessage success() {
        return ResponseMessage.builder ()
                .header(ResponseMessageHeader.builder ()
                        .message ("")
                        .result (true)
                        .resultCode ("")
                        .status (HttpStatus.OK.value ())
                        .build ())
                .body("")
                .build ();
    }
}

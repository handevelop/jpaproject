package com.example.jpa.common;

import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.user.model.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseResult {

    public static ResponseEntity<?> fail (String message) {
        return ResponseEntity.badRequest ().body (ResponseMessage.fail (message));
    }

    public static ResponseEntity<?> fail (String message, Object data) {
        return ResponseEntity.badRequest ().body (ResponseMessage.fail (message, data));
    }

    public static ResponseEntity<?> success () {
        return success (null);
    }

    public static ResponseEntity<?> result (ServiceResult serviceResult) {
        if (serviceResult.isFail ()) {
            return ResponseResult.fail (serviceResult.getMessage ());
        }
        return ResponseResult.success ();
    }

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok ().body (ResponseMessage.success (data));

    }
}

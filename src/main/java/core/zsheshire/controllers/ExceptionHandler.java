package core.zsheshire.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Map<String, Object>> shezireException(ShezireException exception) {

        Map<String, Object> response = new HashMap<>();
        response.put("reason", exception.getDesc());
        response.put("code", exception.getCode());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
}

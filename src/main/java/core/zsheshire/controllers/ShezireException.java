package core.zsheshire.controllers;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShezireException extends Throwable {

    public ShezireException() {

    }

    public ShezireException(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    private String desc;

    private String code;

}

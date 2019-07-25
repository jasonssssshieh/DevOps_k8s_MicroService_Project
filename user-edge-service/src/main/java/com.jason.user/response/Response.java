package com.jason.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable{

    public static final Response USERNAME_PASSWORD_INVALIDATE = new Response("1001", "username or/and password is error.");
    private String code;
    private String message;

    public Response(String code, String message){
        this.code = code;
        this.message = message;
    }
}

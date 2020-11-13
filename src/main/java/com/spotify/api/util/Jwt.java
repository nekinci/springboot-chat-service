package com.spotify.api.util;

import com.spotify.api.models.UserToken;

import java.time.LocalDateTime;

public class Jwt {

    /**
     *
     * ***************************************************
     *
     * If token expired return false, else return true
     * @author Niyazi Ekinci
     * @version 1.0
     *
     * ****************************************************
     *
     * */
    public static boolean checkToken(UserToken token){
        return !(token.getLastChangeAt().getSecond() + token.getExpiresIn() <= LocalDateTime.now().getSecond());
    }
}

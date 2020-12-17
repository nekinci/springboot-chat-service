package com.spotify.api.service.authentication;
import com.spotify.api.dto.TokenDto;
import com.spotify.api.model.User;
import com.spotify.api.model.valueObject.UserToken;
import com.spotify.api.service.external.SpotifyService;
import com.spotify.api.service.UserService;
import com.spotify.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    SpotifyService spotifyService;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    public TokenDto authorize(String code){

        UserToken result = spotifyService.getToken(code);
        if (result == null)
            return null;
        com.wrapper.spotify.model_objects.specification.User user = spotifyService.me(result);
        if(user == null)
            return null;
        User _user = userService.createUserOrUpdateToken(user, result);
        String token = jwtTokenUtil.generateToken(_user);
        TokenDto tokenDto = TokenDto.builder().token(token).refreshToken("").expiresIn(jwtTokenUtil.getExpirationDateFromToken(token).getTime()).build();
        return tokenDto;

    }

}

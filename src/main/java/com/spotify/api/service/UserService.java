package com.spotify.api.service;

import com.spotify.api.core.abstraction.MQService;
import com.spotify.api.dto.MailDto;
import com.spotify.api.model.valueObject.Detail;
import com.spotify.api.model.User;
import com.spotify.api.model.valueObject.UserToken;
import com.spotify.api.repository.UserRepository;
import com.spotify.api.util.JwtTokenUtil;
import com.spotify.api.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServletUtil servletUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MQService mqService;
    public User createUserOrUpdateToken(com.wrapper.spotify.model_objects.specification.User user, UserToken token){
        User _user = userRepository.findBySpotifyId(user.getId());

        if(Objects.isNull(_user)){
            String[] names = user.getDisplayName().split(" ");
            token.setLastChangeAt(LocalDateTime.now());
            _user = User.builder()
                    .name(names.length >= 1 ? names[0] : "Unnamed")
                    .surname(names.length >= 2 ? names[1]: "")
                    .email(user.getEmail())
                    .spotifyId(user.getId())
                    .userToken(token)
                    .createDate(new Date())
                    .loginDetails(new ArrayList<>())
                    .build();

        }
        else {
            UserToken userToken = _user.getUserToken();
            userToken.setToken(token.getToken());
            userToken.setRefreshToken(token.getRefreshToken());
            userToken.setLastChangeAt(LocalDateTime.now());
            _user.setUserToken(userToken);
        }
        Detail _detail = Detail.builder()
                .agent(servletUtil.getHeader("User-Agent"))
                .ipAddress(servletUtil.getIpAddress())
                .loginDate(new Date())
                .build();
        _user.getLoginDetails().add(_detail);
        userRepository.save(_user);
            mqService.sendMail(MailDto.builder()
                    .from("info@songchat.com")
                    .subject("Songchat | Bilgilendirme")
                    .to(_user.getEmail())
                    .deliveryType(MailDto.DeliveryType.GREETING_MAIL)
                    .toFullName(_user.getName() + " " + _user.getSurname())
                    .build());
        return _user;
    }

    public User updateUser(User user){
        if(user == null) return null;
        Optional<User> userData = userRepository.findById(user.getId());
        if (userData.isPresent()){
            User _user = userData.get();
            _user.setAvatar(user.getAvatar());
            _user.setBirthDate(user.getBirthDate());
            _user.setBirthOfPlace(user.getBirthOfPlace());
            _user.setDescription(user.getDescription());
            _user.setHobbyList(user.getHobbyList());
            _user.setLikedStyles(user.getLikedStyles());
            _user.setModifiedDate(new Date());
            userRepository.save(_user);
            return _user;
        }
        return null;
    }


    public User me(){
        String email = jwtTokenUtil.getEmailFromToken(servletUtil.getAuthTokenFromRequest());
        return userRepository.findByEmailAndEndDateNull(email);
    }


    public User getByEmail(String email){
        if(email == null) return null;
        return userRepository.findByEmailAndEndDateNull(email);
    }

    public void setLastSeen(){
        String email = jwtTokenUtil.getEmailFromToken(servletUtil.getAuthTokenFromRequest());
        User user = userRepository.findByEmailAndEndDateNull(email);
        if(user == null) return;
        user.setLastSeenDate(new Date());
        userRepository.save(user);
    }
}

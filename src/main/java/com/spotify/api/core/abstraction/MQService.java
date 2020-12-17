package com.spotify.api.core.abstraction;

import com.spotify.api.dto.MailDto;
import org.springframework.stereotype.Service;

public interface MQService {
    void sendMail(MailDto mail);
}

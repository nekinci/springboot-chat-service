package com.spotify.api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

@Data
@Builder
public class MailDto {

    private String from;
    private String to;
    private String deliveryType;
    private String subject;
    private String text;
    private String html;
    private String toFullName;

    public static class DeliveryType{
        public static String GREETING_MAIL = "GREETING_MAIL";
    }
}

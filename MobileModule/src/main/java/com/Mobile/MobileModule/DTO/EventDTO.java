package com.Mobile.MobileModule.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class EventDTO {

    private String eventType;

    private String employee;

    private Date eventTimeStamp;

    private String objectId;

    private String deviceId;

    private String note;
}

package com.matrix.neo.servcie;

import com.matrix.neo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MailService {
    @Autowired
    private UserService userService;

    private ZoneId zoneId = ZoneId.systemDefault();


    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String getTime() {
        return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void sendLoginMail(User user) {
        System.err.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
    }

    public void sendRegistrationMail(User user) {
        System.err.println(String.format("Welcome, %s!", user.getName()));

    }

    public String sendMail() {
        // ZoneId zoneId = userService.getFinalZoneId(); NPE
        // ZoneId zoneId = userService.getZoneId(); NPE
        ZoneId zoneId = userService.getZoneId(); // ☑️
        String dt = ZonedDateTime.now(zoneId).toString();
        return "Hello, it is " + dt;
    }
}

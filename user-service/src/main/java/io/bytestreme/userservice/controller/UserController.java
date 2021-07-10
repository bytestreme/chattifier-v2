package io.bytestreme.userservice.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
@RequestMapping("user")
public class UserController {

    @SneakyThrows
    @GetMapping("hello")
    public String hello() {
        return "hello from user service - " + InetAddress.getLocalHost();
    }

}

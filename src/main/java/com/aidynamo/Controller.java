package com.aidynamo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping
    public String helloworld(){
        return "We are Live! ";
    }
}

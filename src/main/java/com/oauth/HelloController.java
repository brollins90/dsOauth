package com.oauth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

//    @Autowired
//    private User someUser;

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }
    
    
}

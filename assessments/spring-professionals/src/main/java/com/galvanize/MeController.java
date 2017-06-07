package com.galvanize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/me")
    public String getMe() {
        return "Implement me!!";
    }

}

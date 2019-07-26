package ru.domclick.am.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AccountController {

    @GetMapping(value = "accounts/{id}")
    public String get(@PathVariable String id) {
        return "Hello " + id;
    }
}

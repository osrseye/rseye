package com.basketbandit.rseye.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SiteController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}

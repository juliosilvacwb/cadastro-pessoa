package com.springboot.api.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * IndexController
 */
@Controller
public class IndexController implements ErrorController {

    @RequestMapping(value = "/home")
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }
    
    
    @RequestMapping(value = "/source")
    @ResponseBody
    public String error() {
        return "http://github.com";
    }

    @Override
    @RequestMapping("/error")
    public String getErrorPath() {
        return "index";
    }
    
}
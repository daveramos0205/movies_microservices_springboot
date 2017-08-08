package com.davidango.error.http;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandler implements ErrorController{

    @RequestMapping(value = "/error")
    public String error() {
        return "That resource was not found on this service";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
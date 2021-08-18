package com.example.spanerrordispatch;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping
	public String hello() {
		return "authenticated";
	}
}

package com.skyapi.hellorestapi.web;



import com.skyapi.hellorestapi.entity.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ApiController {

    @GetMapping("/api/hello")
    public Response hello(Principal principal){
      return  new Response ("Hello " + principal.getName () + "!");
    }
}

package edu.du._waxing_home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/people/waxing")
    public String waxing() {return "pages/people/peopleWaxing";}

    @GetMapping("/people/directions")
    public String directions() {return "pages/people/directions";}


}

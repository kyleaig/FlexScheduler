package org.FlexScheduler.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.FlexScheduler.scheduler.*;

@Controller
public class DocController {

    @RequestMapping("/")
    public String schedule(@RequestParam(value="name", required=false, defaultValue="Kyle") String name, Model model) {
    	Calendar MLC = new Calendar(0);
        model.addAttribute("name", name);
        model.addAttribute("cal", MLC);
        return "index";
    }
}
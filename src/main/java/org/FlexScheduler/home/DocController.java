package org.FlexScheduler.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocController {

    @RequestMapping("/home")
    public String schedule(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }
    
    @RequestMapping("/")
    public String index() {
    	return "index";
    }

}

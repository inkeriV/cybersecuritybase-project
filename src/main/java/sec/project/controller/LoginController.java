/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    
    final static String adminpw = "a4tG2ivnk70q9";
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String loginForm() {
        
        return "login";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes ra) {
        
        if (!username.equals("admin") || !password.equals(adminpw)) {
            ra.addFlashAttribute("message", "Wrong credentials.");
            return "redirect:/message";
        }

        return "redirect:/admin/show";
    }
    
}

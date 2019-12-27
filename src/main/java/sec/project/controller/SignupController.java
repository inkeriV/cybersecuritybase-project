package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    final static String pw = "w3Lc0M32p4rTy!";   
    
    public List<String> guests = new ArrayList<>();
    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }
    
    @Transactional
    @RequestMapping(value = "/form", method = RequestMethod.POST) 
    public String submitForm(@RequestParam String username, @RequestParam String name, 
                             @RequestParam String number, @RequestParam String email, 
                             @RequestParam String password, RedirectAttributes ra) {
        
        if (password.equals(pw) && username.equals("admin")) {
            ra.addFlashAttribute("message", "Username not allowed.");
            return "redirect:/message";
        }
        if ((!password.equals(pw) && username.equals("admin")) || !password.equals(pw)) {
            ra.addFlashAttribute("message", "Password is not correct.");
            return "redirect:/message";
        }
        if (username=="" || name=="" || number=="" || email=="") {
            ra.addFlashAttribute("message", "Some information was missing.");
            return "redirect:/message";
        }
        for (String u : guests) {
            if (u.equals(username)) {
                ra.addFlashAttribute("message", "Username already taken.");
                return "redirect:/message";
            }
        }
        guests.add(username);
        entityManager.createNativeQuery("insert into Guest (username, name, phonenumber, email) values ('" + username + "', '" + name + "', '" + number + "', '" + email + "')").executeUpdate();
        return "done";
    }
    
    @RequestMapping("/message")
    public String message(Model model) {
        return "message";
    }

}


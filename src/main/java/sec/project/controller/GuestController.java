/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import org.springframework.stereotype.Controller;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sec.project.domain.Guest;

@Controller
public class GuestController {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @RequestMapping("/find")
    public String defaultMapping1() {
        return "redirect:/search";
    }
    
    @RequestMapping(value="/search", method = RequestMethod.GET) 
    public String loadLogin() {
        return "search";
    }
    
    @RequestMapping(value="/search", method = RequestMethod.POST) 
    public String submitUsername(@RequestParam String username, RedirectAttributes ra) {
        
        Query query = entityManager.createNativeQuery("select * from Guest where username = '" + username + "'", Guest.class);
        List<Guest> g = query.getResultList();
        
        if(!g.isEmpty()) {
            
            ra.addFlashAttribute("guest", g);
            return "redirect:/"+ username +"/show"; 
        }
       ra.addFlashAttribute("message", "No guest found with given username.");
       
       return "redirect:/message";
        
    }
    
    @RequestMapping(value="/{username}/show", method = RequestMethod.GET)
    public String showInfo(Model model, RedirectAttributes ra, @PathVariable("username") String username) { 
        
        Query query = entityManager.createNativeQuery("select * from Guest where username = '" + username + "'", Guest.class);
        List<Guest> g = query.getResultList();
        model.addAttribute("guest", g);
        
        return "show";
    }
    
    @RequestMapping(value="/admin/show", method = RequestMethod.GET)
    public String getAll(Model model) {
        
        Query query = entityManager.createNativeQuery("Select * from Guest", Guest.class);
        List<Guest> guests = query.getResultList();
        model.addAttribute("guest", guests);
        
        return "adminshow";
    }
    
    @Transactional
    @RequestMapping(value="/delete/{username}", method = RequestMethod.POST)
    public String deleteGuest(@PathVariable("username") String username) {
        
        entityManager.createNativeQuery("delete from Guest where username = '" + username + "'").executeUpdate();
        
        return "redirect:/admin/show";
    }
}

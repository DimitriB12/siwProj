package it.uniroma3.siw.siwProj.controller;

import it.uniroma3.siw.siwProj.controller.session.SessionData;
import it.uniroma3.siw.siwProj.controller.validation.UserValidator;
import it.uniroma3.siw.siwProj.model.Credentials;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.repository.UserRepository;
import it.uniroma3.siw.siwProj.service.CredentialsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserValidator userValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;
    
    @Autowired
	CredentialsService credentialService;

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "home";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    
    /**
     * This method is called when a GET request is snet by the User to the URL:"/admin/users".
     * This method prepares and dispatches the view with the list of all users for admin usage. 
     * @param model the Request model
     * @return the name of the target view
     */
    @RequestMapping(value = {"/admin/users" }, method = RequestMethod.GET)
    public String usersList(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Credentials> allCredentials = this.credentialService.getAllCredentials();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("credentialsList", allCredentials);
    	return "allUsers";
    }
    
    
    /**This method is called when a POST request is sent by the User to the URL:"/admin/users/{username}/delete",
     * and deletes the User with the Credentials identified by the by the passed username  {username}.
     * @param model the Request model
     * @param username of the Credentials to be deleted
     * @return the name of the target view
     */
    @RequestMapping(value= { "/admin/users/{username}/delete" }, method = RequestMethod.POST)
    public String removeUser(Model model, @PathVariable String username) {
    	this.credentialService.deleteCredentials(username);
    	return "redirect:/admin/users";
    }

}

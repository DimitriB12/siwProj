package it.uniroma3.siw.siwProj.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.siwProj.controller.session.SessionData;
import it.uniroma3.siw.siwProj.controller.validation.ProjectValidator;
import it.uniroma3.siw.siwProj.controller.validation.TaskValidator;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.CredentialsService;
import it.uniroma3.siw.siwProj.service.ProjectService;
import it.uniroma3.siw.siwProj.service.TaskService;
import it.uniroma3.siw.siwProj.service.UserService;

@Controller
public class TaskController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	TaskService taskService;
	
	
    @RequestMapping(value = {"/task/editTask" }, method = RequestMethod.GET)
    public String taskEditList(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Project> projectsList = this.projectService.retriveProjectsOwnedBy(loggedUser);
    	List<Task> tasksList = new LinkedList<Task>();
    	
    	for(Project project : projectsList) {
    		tasksList.addAll(project.getTasks());
    	}
     
    	model.addAttribute("tasksList", tasksList);
    	return "taskToEdit";
    }
    
    
  
    @RequestMapping(value= { "/task/editTask/{name}/update" }, method = RequestMethod.POST)
    public String editTask(Model model, @PathVariable String name) {
    
      this.taskService.setCompleted(this.taskService.findTaskByName(name));
    	
      return "redirect:/projects";
    }
	
	
	
	
	

}

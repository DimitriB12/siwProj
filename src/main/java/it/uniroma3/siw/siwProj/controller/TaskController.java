package it.uniroma3.siw.siwProj.controller;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.siwProj.controller.session.SessionData;
import it.uniroma3.siw.siwProj.controller.validation.ProjectValidator;
import it.uniroma3.siw.siwProj.controller.validation.TaskValidator;
import it.uniroma3.siw.siwProj.model.ContenitoreStringhe;
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
	
	
	
	//Edit a Task for make it Completed
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
	
    
    
    //Assaigns a Task to a member
    
    
    
    @RequestMapping(value = {"/task/assaignsTask"}, method = RequestMethod.GET)
    public String assaignsTaskForm(Model model) {
    
     model.addAttribute("assaignsForm", new ContenitoreStringhe());
    		
      return "assaignsTaskToMember";
    }
    
    @RequestMapping(value = {"/task/assaignsTask"}, method = RequestMethod.POST)
    public String assaignsTask(Model model, @ModelAttribute("assaignsForm") ContenitoreStringhe assaignsForm) {
    
    	
         if(  this.taskService.findTaskByName(assaignsForm.getTaskName()) != null &&
    		 this.credentialsService.getCredentials(assaignsForm.getUserName()) !=null &&
    		 this.userService.getTaskWorker(this.taskService.findTaskByName(assaignsForm.getTaskName())) == null) {
    		
    	   	Task task = this.taskService.findTaskByName(assaignsForm.getTaskName());
    		User user = this.credentialsService.getCredentials(assaignsForm.getUserName()).getUser();
    		
    		for(Project project : user.getVisibleProjects()) {
    			
    		    	if(project.getTasks().contains(task)) {
    			       this.userService.assaignTaskToUser(task, user);
    			       this.taskService.addWorkerToTask(task, user);
    			       return "redirect:/projects";
    		     	}
             }
    	}
    		 
        return "assaignsTaskToMember";
    }
    
    
    //Show Logged User Tasks
    @RequestMapping(value = {"/task/showTasks"}, method = RequestMethod.GET)
    public String showTaskForm(Model model) {
     User loggedUser = this.sessionData.getLoggedUser();
     
     model.addAttribute("tasksList", this.taskService.findTasksByUser(loggedUser));
    		
      return "myTasks";
    }
    
    /**
	 * This method is called when a GET request is sent by the user to URL "/task".
	 * This method prepares and dispatches a view showing the project with the specified id.
	 * 
	 * @param model the Request model
	 * @param id the Id of the Task
	 * @return the name of the target view, in this case a redirect to "/task"
	 */
	@RequestMapping(value= {"/task/{taskId}"}, method = RequestMethod.GET)
	public String task(Model model, @PathVariable Long taskId) {
		//User loggedUser = sessionData.getLoggedUser();
		//if no project with the passed IO exists,
		//redirect to the view with the list of my projects
		Task task= taskService.getTask(taskId);
		if(task == null)
			return "redirect:/projects";

//		//if i do not have access to any project with the passed ID
//		//redirect to the view with the list of my projects
//		List<User> members = userService.getMembers(project);
//		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
//			return "redirect:/projects";
//
//		model.addAttribute("loggedUser", loggedUser);
//		model.addAttribute("project", project);
		model.addAttribute("task", task);

		return "task";
	}
    
  
    
    
    
    
	
	
	
	

}

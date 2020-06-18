package it.uniroma3.siw.siwProj.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.siwProj.controller.session.SessionData;
import it.uniroma3.siw.siwProj.controller.validation.ProjectValidator;
import it.uniroma3.siw.siwProj.controller.validation.TaskValidator;
import it.uniroma3.siw.siwProj.model.Comment;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.CommentService;
import it.uniroma3.siw.siwProj.service.CredentialsService;
import it.uniroma3.siw.siwProj.service.ProjectService;
import it.uniroma3.siw.siwProj.service.TaskService;
import it.uniroma3.siw.siwProj.service.UserService;

@Controller
public class CommentController {
 
	@Autowired
	protected CommentService commentService;
	
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
	
	
	
	//Add a Comment to a Task
    @RequestMapping(value = {"/addComment/{taskId}"}, method = RequestMethod.GET)
    public String addCommentForm(Model model, @PathVariable Long taskId ) {
    	
      Task task = this.taskService.getTask(taskId);
      model.addAttribute("task", task);
      model.addAttribute("comment", new Comment());
    
    	return "addCommentToTask";
    }
	
    @RequestMapping(value = {"/addComment/{taskId}"}, method = RequestMethod.POST)
    public String addComment(Model model, @PathVariable Long taskId,
    		                     @ModelAttribute("comment") Comment comment) {
       
    	Task task = this.taskService.getTask(taskId);
    	
    	this.taskService.addCommentToTask(comment, task);
    	this.commentService.addTask(comment, task);
       
    
       return "redirect:/task/" + task.getId();
    }
	
	
}

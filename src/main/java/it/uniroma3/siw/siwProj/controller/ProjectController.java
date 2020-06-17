package it.uniroma3.siw.siwProj.controller;

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
import it.uniroma3.siw.siwProj.model.ContenitoreStringhe;
import it.uniroma3.siw.siwProj.model.Credentials;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.CredentialsService;
import it.uniroma3.siw.siwProj.service.ProjectService;
import it.uniroma3.siw.siwProj.service.UserService;

@Controller
public class ProjectController {
	
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
	
	/**
	 * This method is called when a GET request is sent by the user to URL "/projects".
	 * This method prepares and dispatches a view showing all the project owned by the logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that is this case is "myOwnedProjects"
	 */
	@RequestMapping(value = {"/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retriveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "myOwnedProjects";
	}
	
	@RequestMapping(value= {"/projects/{projectId}"}, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		//if no project with the passed IO exists,
		//redirect to the view with the list of my projects
	    Project project= projectService.getProject(projectId);
		if(project == null)
			return "redirect:/projects";
		
		//if i do not have access to any project with the passed ID
		//redirect to the view with the list of my projects
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		
		return "project";
	}
	
   /**
    * This method is called when a Get request is sent by the user to Url "projects/add".
    * This method prepare and dispatches a view containing a form to add a new Project.
    * 
    *  @param model the Request model
    *  @return the name of target view, that in this case is "addProject"
   */
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
	    model.addAttribute("loggedUser", loggedUser);
	    model.addAttribute("projectForm", new Project());
		return "addProject";
	}

	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			                   BindingResult projectBindingResult,
			                   Model model) {
		User loggedUser = sessionData.getLoggedUser();
		
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";
	}
	
	//******************DA MODIFICARE**********************
	
	@RequestMapping(value = {"/shareProject"}, method = RequestMethod.GET)
	public String shereProject(Model model) {
		model.addAttribute("projectUserForm", new ContenitoreStringhe());
		
		return "shareProjectWith";
	}
	
	@RequestMapping(value = {"/shareProject"}, method = RequestMethod.POST)
	public String shereProject(@ModelAttribute("projectUserForm") ContenitoreStringhe projectUserForm, Model model) {
		
     User loggedUser= this.sessionData.getLoggedUser();
	
	if(this.projectService.findProjectByName(projectUserForm.getNameProject()) != null
		  && this.credentialsService.getCredentials(projectUserForm.getUserName()) !=null
		  && !loggedUser.equals(this.credentialsService.getCredentials(projectUserForm.getUserName()).getUser())) {
		    Project project =	this.projectService.findProjectByName(projectUserForm.getNameProject());
		    User user = this.credentialsService.getCredentials(projectUserForm.getUserName()).getUser();
		    this.projectService.shareProjectWithUser(project, user);
		    return "redirect:/projects/" + project.getId();
		}
			
		
		return "redirect:/projects";
	}
	
	
	/**
	 * This method is called when a GET request is sent by the user to URL "/projects".
	 * This method prepares and dispatches a view showing all the project owned by the logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that is this case is "myOwnedProjects"
	 */
	@RequestMapping(value = {"/shared/projects" }, method = RequestMethod.GET)
	public String projectsSharedWithMe(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retriveProjectsWithVisibility(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "projectsSharedWithMe";
	}
	
	@RequestMapping(value= {"/shared/projects/{projectId}"}, method = RequestMethod.GET)
	public String projectsShared(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		//if no project with the passed IO exists,
		//redirect to the view with the list of my projects
	    Project project= projectService.getProject(projectId);
		if(project == null)
			return "redirect:/shared/projects";
		
		//if i do not have access to any project with the passed ID
		//redirect to the view with the list of my projects
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		
		return "project";
	}

	  /**
     * This method is called when a GET request is snet by the User to the URL:"/admin/users".
     * This method prepares and dispatches the view with the list of all users for admin usage. 
     * @param model the Request model
     * @return the name of the target view
     */
    @RequestMapping(value = {"/deleteProject" }, method = RequestMethod.GET)
    public String projectsDeleteList(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Project> projectsList = this.projectService.retriveProjectsOwnedBy(loggedUser);
    
    	//model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("projectsList", projectsList);
    	return "projectToDelete";
    }
    
    
    /**This method is called when a POST request is sent by the User to the URL:"/admin/users/{username}/delete",
     * and deletes the User with the Credentials identified by the by the passed username  {username}.
     * @param model the Request model
     * @param username of the Credentials to be deleted
     * @return the name of the target view
     */
    @RequestMapping(value= { "/deleteProject/{name}/delete" }, method = RequestMethod.POST)
    public String removeProject(Model model, @PathVariable String name) {
    	this.projectService.deleteProject(this.projectService.findProjectByName(name));
    	return "redirect:/projects";
    }
	
	  


}

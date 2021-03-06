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
import it.uniroma3.siw.siwProj.model.Credentials;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.CredentialsService;
import it.uniroma3.siw.siwProj.service.ProjectService;
import it.uniroma3.siw.siwProj.service.TaskService;
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

	@Autowired
	TaskValidator taskValidator;

	@Autowired
	TaskService taskService;

	/**
	 * This method is called when a GET request is sent by the user to URL "/projects".
	 * This method prepares and dispatches a view showing all the project owned by the logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that is this case is "myOwnedProjects"
	 */
	@RequestMapping(value = {"/projects"}, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retriveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "myOwnedProjects";
	}


	/**
	 * This method is called when a GET request is sent by the user to URL "/projects/{projectId}".
	 * This method prepares and dispatches a view showing the project with the specified id.
	 * 
	 * @param model the Request model
	 * @param id the Id of the Project
	 * @return the name of the target view, in this case a redirect to "/projects"
	 */
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
	 * This method is called when a GET request is sent by the user to URL "projects/add".
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


	/**This method is called when a POST request is sent by the User to the URL:"/projects/add",
	 * and creates a new Project based on the projectForm compiled by the User.
	 * @param model the Request model
	 * @param project the Project to be created
	 * @return the name of the target view, in this case "addProject"
	 */
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

	/******************DA MODIFICARE**********************/


	/**
	 * This method is called when a GET request is sent by the user to URL "/shareProject".
	 * This method prepare and dispatches a view containing a form to share a Project with a User.
	 * 
	 *  @param model the Request model
	 *  @return the name of target view, that in this case is "shareProjectWith"
	 */
	@RequestMapping(value = {"/shareProject"}, method = RequestMethod.GET)
	public String shereProject(Model model) {
		model.addAttribute("projectUserForm", new ContenitoreStringhe());

		return "shareProjectWith";
	}


	/**This method is called when a POST request is sent by the User to the URL:"/shareProject",
	 * and shares a Project to a User based on the projectUserForm.
	 * @param model the Request model
	 * @param projectUserForm a Sting container to recieve User username and Project name
	 * @return the name of the target view, in this case a redirect to "/projects"
	 */
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
	 * This method is called when a GET request is sent by the user to URL "/shared/projects".
	 * This method prepares and dispatches a view showing all the project shared to the logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that is this case is "projectsSharedWithMe"
	 */
	@RequestMapping(value = {"/shared/projects"}, method = RequestMethod.GET)
	public String projectsSharedWithMe(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retriveProjectsWithVisibility(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "projectsSharedWithMe";
	}

	/**
	 * This method is called when a GET request is sent by the user to URL "/shared/projects/{projectsId}".
	 * This method prepares and dispatches a view showing all members for the Project with the specifies ID.
	 * 
	 * @param model the Request model
	 * @param id the Id of the Project
	 * @return the name of the target view, that is this case is "project"
	 */
	@RequestMapping(value= {"/shared/projects/{projectId}"}, method = RequestMethod.GET)
	public String projectsShared(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		//if no project with the passed ID exists, redirect to the view with the list of my projects
		Project project= projectService.getProject(projectId);
		if(project == null)
			return "redirect:/shared/projects";

		//if i do not have access to any project with the passed ID, redirect to the view with the list of my projects
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";

		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);

		return "project";
	}

	/**
	 * This method is called when a GET request is sent by the User to the URL:"/deleteProject".
	 * This method prepares and dispatches the view with the list of all owned projects. 
	 * @param model the Request model
	 * @return the name of the target view, in this case "projectToDelete"
	 */
	@RequestMapping(value = {"/deleteProject"}, method = RequestMethod.GET)
	public String projectsDeleteList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = this.projectService.retriveProjectsOwnedBy(loggedUser);

		model.addAttribute("projectsList", projectsList);
		return "projectToDelete";
	}


	/**This method is called when a POST request is sent by the User to the URL:"/deleteProject/{name}/delete",
	 * and deletes the Project  identified by the by the passed name {name}.
	 * @param model the Request model
	 * @param name of the Project to be deleted
	 * @return the name of the target view, in this case a redirect to "/projects"
	 */
	@RequestMapping(value= {"/deleteProject/{name}/delete"}, method = RequestMethod.POST)
	public String removeProject(Model model, @PathVariable String name) {
		this.projectService.deleteProject(this.projectService.findProjectByName(name));
		return "redirect:/projects";
	}

	/**
	 * This method is called when a GET request is sent by the User to the URL:"/project/addTask".
	 * This method prepares and dispatches the view with the list of all owned projects. 
	 * @param model the Request model
	 * @return the name of the target view, in this case "addTask"
	 */
	@RequestMapping(value= {"/project/addTask"}, method = RequestMethod.GET)
	public String addTask(Model model) {

		model.addAttribute("taskForm", new Task());
		model.addAttribute("projName", new ContenitoreStringhe());
		return "addTask";
	}

	/**This method is called when a POST request is sent by the User to the URL:"/project/addTask",
	 * and creates a Task based on the taskForm compiled.
	 * @param model the Request model
	 * @param task the Task to be created
	 * @return the name of the target view, in this case a redirect to "/addTask"
	 */
	@RequestMapping(value= {"/project/addTask"}, method = RequestMethod.POST)
	public String addTaskToProject(Model model, @Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult,
			@ModelAttribute("projName") ContenitoreStringhe projName) {

		User loggedUser = this.sessionData.getLoggedUser();
		List<Project> listProjects = this.projectService.retriveProjectsOwnedBy(loggedUser);

		this.taskValidator.validate(task, taskBindingResult);
		if(!taskBindingResult.hasErrors() && this.projectService.findProjectByName(projName.getNameProject()) != null 
				&& listProjects.contains(this.projectService.findProjectByName(projName.getNameProject()))) {
			Project project = this.projectService.findProjectByName(projName.getNameProject());
			
			project.getTasks().add(task);
			this.taskService.saveTask(task);
			return "redirect:/projects/" + project.getId();

		}

		return "addTask";
	}

	/**
	 * This method is called when a GET request is sent by the User to the URL:"/deleteTask".
	 * This method prepares and dispatches the view with the list of all tasks of the projects owned. 
	 * @param model the Request model
	 * @return the name of the target view, in this case "taskToDelete"
	 */
	@RequestMapping(value = {"/project/deleteTask"}, method = RequestMethod.GET)
	public String taskDeleteList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = this.projectService.retriveProjectsOwnedBy(loggedUser);
		List<Task> tasksList = new LinkedList<Task>();

		for(Project project : projectsList) {
			tasksList.addAll(project.getTasks());
		}
		model.addAttribute("tasksList", tasksList);
		return "taskToDelete";
	}

	/**This method is called when a POST request is sent by the User to the URL:"/deleteTask/{name}/delete",
	 * and deletes the Task identified by the by the passed name {name}.
	 * @param model the Request model
	 * @param name of the Task to be deleted
	 * @return the name of the target view, in this case a redirect to "/projects"
	 */
	@RequestMapping(value= {"/project/deleteTask/{name}/delete"}, method = RequestMethod.POST)
	public String removeTask(Model model, @PathVariable String name) {
		this.taskService.deleteTask(this.taskService.findTaskByName(name));
		return "redirect:/projects";
	}
	





}

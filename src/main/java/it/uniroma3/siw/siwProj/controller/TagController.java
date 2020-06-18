package it.uniroma3.siw.siwProj.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.siwProj.controller.session.SessionData;
import it.uniroma3.siw.siwProj.controller.validation.TagValidator;
import it.uniroma3.siw.siwProj.model.ContenitoreStringhe;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Tag;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.ProjectService;
import it.uniroma3.siw.siwProj.service.TagService;

@Controller
public class TagController {

	@Autowired
	TagService tagService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TagValidator tagValidator;

	
	/**
	 * This method is called when a GET request is sent by the User to the URL:"/project/addTag".
	 * This method prepares and dispatches the view with the list of all owned projects. 
	 * @param model the Request model
	 * @return the name of the target view, in this case "addTag"
	 */
	@RequestMapping(value= {"/project/addTag"}, method = RequestMethod.GET)
	public String addTag(Model model) {

		model.addAttribute("tagForm", new Tag());
		model.addAttribute("projName", new ContenitoreStringhe());
		return "addTag";
	}

	/**This method is called when a POST request is sent by the User to the URL:"/project/addTag",
	 * and creates a Tag based on the tagForm compiled.
	 * @param model the Request model
	 * @param tag the Tag to be created
	 * @return the name of the target view, in this case a redirect to "/addTag"
	 */
	@RequestMapping(value= {"/project/addTag"}, method = RequestMethod.POST)
	public String addTagToProject(Model model, @Valid @ModelAttribute("tagForm") Tag tag,
			BindingResult tagBindingResult,
			@ModelAttribute("projName") ContenitoreStringhe projName) {

		User loggedUser = this.sessionData.getLoggedUser();
		List<Project> listProjects = this.projectService.retriveProjectsOwnedBy(loggedUser);

		this.tagValidator.validate(tag, tagBindingResult);
		if(!tagBindingResult.hasErrors() && this.projectService.findProjectByName(projName.getNameProject()) != null 
				&& listProjects.contains(this.projectService.findProjectByName(projName.getNameProject()))) {
			Project project = this.projectService.findProjectByName(projName.getNameProject());
			project.getTags().add(tag);
			this.tagService.saveTag(tag);
			return "redirect:/projects/" + project.getId();

		}

		return "addTag";
	}

	}

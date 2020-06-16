package it.uniroma3.siw.siwProj.controller.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.service.ProjectService;

@Component
public class ProjectValidator implements Validator {
	
	final Integer MAX_NAME_LENGHT = 100;
	final Integer MIN_NAME_LENGHT = 2;
	final Integer MAX_DESCRIPTION_LENGHT=1000;

	@Autowired
	ProjectService projectService;

	@Override
	public void validate(Object o, Errors errors) {
		
		Project project =(Project) o;
		String name = project.getName().trim();
		String description = project.getDescription().trim();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		if(name.length() < this.MIN_NAME_LENGHT || name.length() > this.MAX_NAME_LENGHT)
			errors.rejectValue("name", "size");
		if(description.length() > this.MAX_DESCRIPTION_LENGHT)
			errors.rejectValue("description", "size");
		
		
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Project.class.equals(clazz);
	}

}

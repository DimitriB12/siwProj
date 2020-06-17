package it.uniroma3.siw.siwProj.controller.validation;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.service.TaskService;

@Component
public class TaskValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH=1000;

	@Autowired
	TaskService taskService;

	@Override
	public void validate(Object o, Errors errors) {
		
		Task task =(Task) o;
		String name = task.getName().trim();
		String description = task.getDescription().trim();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");

		if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if (this.taskService.findTaskByName(name) != null)
			errors.rejectValue("name", "duplicate");
		if(description.length() > this.MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");
		
		
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Project.class.equals(clazz);
	}

}

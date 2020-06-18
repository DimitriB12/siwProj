package it.uniroma3.siw.siwProj.controller.validation;

import org.springframework.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Tag;
import it.uniroma3.siw.siwProj.service.TagService;

@Component
public class TagValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_COLOR_LENGTH = 20;
	final Integer MIN_COLOR_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH=100;
	
	@Autowired
	TagService tagService;
	
	@Override
	public void validate(Object o, Errors errors) {
		
		Tag tag = (Tag) o;
		String name = tag.getName().trim();
		String color = tag.getColor().trim();
		String description = tag.getDescription().trim();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "color", "required");
		
		if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if (this.tagService.findTagByName(name) != null)
			errors.rejectValue("name", "duplicate");
		if (color.length() < MIN_COLOR_LENGTH || color.length() > MAX_COLOR_LENGTH)
			errors.rejectValue("name", "size");
		else if (this.tagService.findTagByColor(color) != null)
			errors.rejectValue("name", "duplicate");
		if(description.length() > this.MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");		
	}
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Project.class.equals(clazz);
	}

}

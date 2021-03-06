package it.uniroma3.siw.siwProj.controller.validation;

import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for User
 */
@Component
public class UserValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;

    @Autowired
    UserService userService;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String firstName = user.getFirstName().trim();
        String lastName = user.getLastName().trim();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "required");
        
        if (firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH)
            errors.rejectValue("firstName", "size");
        
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "required");

        if (lastName.length() < MIN_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH)
            errors.rejectValue("lastName", "size");
        
        
        
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

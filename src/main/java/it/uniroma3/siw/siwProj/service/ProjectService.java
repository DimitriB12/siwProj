package it.uniroma3.siw.siwProj.service;

import it.uniroma3.siw.siwProj.model.Credentials;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ProjectService handles logic for Projects.
 * This mainly consists in CRUD operations, as well as sharing projects with other users.
 */
@Service
public class ProjectService {

    @Autowired
    protected ProjectRepository projectRepository;

    /**
     * This method retrieves a Project from the DB based on its ID.
     * @param id the id of the Project to retrieve from the DB
     * @return the retrieved Project, or null if no Project with the passed ID could be found in the DB
     */
    @Transactional
    public Project getProject(long id) {
        Optional<Project> result = this.projectRepository.findById(id);
        return result.orElse(null);
    }
    
    @Transactional
    public Project findProjectByName(String projectName) {
    	Optional<Project> result = this.projectRepository.findByName(projectName);
    	return result.orElse(null);
    }

    /**
     * This method saves a Project in the DB.
     * @param project the Project to save into the DB
     * @return the saved Project
     */
    @Transactional
    public Project saveProject(Project project) {
        return this.projectRepository.save(project);
    }

    /**
     * This method deletes a Project from the DB.
     * @param project the Project to delete from the DB
     */
    @Transactional
    public void deleteProject(Project project) {
        this.projectRepository.delete(project);
    }

    /**
     * This method saves a Project among the ones shared with a specific User.
     * @param project the Project to share with the User
     * @param user the User with to share the Project to
     * @return the shared Project
     */
    @Transactional
    public Project shareProjectWithUser(Project project, User user) {
        project.addMember(user);
        return this.projectRepository.save(project);
    }
    
   
    
    
    @Transactional
    public List<Project> retriveProjectsOwnedBy(User loggedUser){
       List<Project> result = this.projectRepository.findByOwner(loggedUser);
       return result;
    }
    
    @Transactional
    public List<Project> getAllProjects() {
        List<Project> result = new ArrayList<>();
        Iterable<Project> iterable = this.projectRepository.findAll();
        for(Project projects : iterable)
            result.add(projects);
        return result;
    }
    
    @Transactional
    public List<Project> retriveProjectsWithVisibility(User loggedUser){
    	List<Project> result = this.projectRepository.findByMembers(loggedUser);
    	return result;
    }
 }

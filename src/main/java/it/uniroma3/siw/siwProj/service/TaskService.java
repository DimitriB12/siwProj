package it.uniroma3.siw.siwProj.service;

import it.uniroma3.siw.siwProj.model.Comment;
import it.uniroma3.siw.siwProj.model.Project;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.model.User;
import it.uniroma3.siw.siwProj.repository.TaskRepository;
import it.uniroma3.siw.siwProj.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The TaskRepository handles logic for Tasks.
 */
@Service
public class TaskService {

    @Autowired
    protected TaskRepository taskRepository;
    
    @Autowired
    protected UserRepository userRepository;

    /**
     * This method retrieves a Task from the DB based on its ID.
     * @param id the id of the Task to retrieve from the DB
     * @return the retrieved Task, or null if no Task with the passed ID could be found in the DB
     */
    @Transactional
    public Task getTask(long id) {
        Optional<Task> result = this.taskRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * This method saves a Task in the DB.
     * @param task the Task to save into the DB
     * @return the saved Task
     */
    @Transactional
    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    }

    /**
     * This method sets a Task in the DB as completed.
     * @param task the Task to set as completed
     * @return the task, after it has been set as completed and flushed in to the DB
     */
    @Transactional
    public Task setCompleted(Task task) {
        task.setCompleted(true);
        return this.taskRepository.save(task);
    }


    /**
     * This method deletes a Task from the DB.
     * @param task the Task to delete from the DB
     */
    @Transactional
    public void deleteTask(Task task) {
        this.taskRepository.delete(task);
    }
    
    @Transactional
    public Task findTaskByName(String taskName) {
    	Optional<Task> result = this.taskRepository.findByName(taskName);
    	return result.orElse(null);
    }
    
   
    
    @Transactional
    public List<Task> findTasksByUser(User user) {
    	return this.taskRepository.findByWorker(user);
    }
    
    @Transactional
    public Task addWorkerToTask(Task task ,User user) {
    	task.setWorker(user);
    	return this.taskRepository.save(task);
    }
    
    @Transactional
	 public Task addCommentToTask(Comment comment, Task task) {
		 task.addComment(comment);
		 return this.taskRepository.save(task);
	 }
}

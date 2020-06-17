package it.uniroma3.siw.siwProj.repository;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


import it.uniroma3.siw.siwProj.model.Task;

/**
 * This interface is a CrudRepository for repository operations on Tasks.
 *
 * @see Task
 */
public interface TaskRepository extends CrudRepository<Task, Long> {
	
	public Optional<Task> findByName(String name);

}


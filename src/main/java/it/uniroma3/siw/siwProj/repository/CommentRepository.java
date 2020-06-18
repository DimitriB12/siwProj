package it.uniroma3.siw.siwProj.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwProj.model.Comment;
import it.uniroma3.siw.siwProj.model.Task;

public interface CommentRepository extends CrudRepository<Comment , Long> {

	
	public List<Comment> findByTask(Task task);
}

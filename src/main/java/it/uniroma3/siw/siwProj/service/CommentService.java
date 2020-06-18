package it.uniroma3.siw.siwProj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siwProj.model.Comment;
import it.uniroma3.siw.siwProj.model.Task;
import it.uniroma3.siw.siwProj.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	protected CommentRepository commentRepository;
	
	 @Transactional
	    public Comment saveComment(Comment comment) {
	        return this.commentRepository.save(comment);
	 }
	 
	 @Transactional
	    public void deleteComment(Comment comment) {
	        this.commentRepository.delete(comment);
	 }
	 
	 @Transactional
	 public List<Comment> findCommentsByTask(Task task){
		 List<Comment> result = this.commentRepository.findByTask(task);
		 return result;
	 }
	 
	 @Transactional
	 public Comment addTask(Comment comment, Task task) {
		 comment.setTask(task);
		 return this.commentRepository.save(comment);
	 }


}

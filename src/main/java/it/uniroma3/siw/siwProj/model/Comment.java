package it.uniroma3.siw.siwProj.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long Id;
	
	@Column(nullable= false)
	private String text;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.REMOVE)
	private Task task;
}

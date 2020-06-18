package it.uniroma3.siw.siwProj.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Tag {
	
	 /**
     * Unique identifier for this Tag
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Name for this Tag
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Description for this Tag
     */
    @Column
    private String description;
    
    /**
     * Color for this Tag
     */
    @Column(nullable = false, length = 100)
    private String color;
    
    @ManyToOne(fetch= FetchType.EAGER)  //if a Tag is retrieved, get also its Project 
    private Project associatedProject;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> associatedTasks;
    
    
    public Tag(){
    	this.associatedTasks = new ArrayList<>();
    }   
    
    public Tag(String name, String description, String color) {
        this();
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    
    //GETTERS AND SETTERS
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setAssociatedProject(Project associatedProject) {
		this.associatedProject = associatedProject;
	}

	public List<Task> getAssociatedTasks() {
		return associatedTasks;
	}

	public void setAssociatedTasks(List<Task> associatedTasks) {
		this.associatedTasks = associatedTasks;
	}
	
 

}

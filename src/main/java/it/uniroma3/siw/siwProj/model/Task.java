package it.uniroma3.siw.siwProj.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Task is a unitary activity managed by the TaskManager.
 * It is generated and owned by a specific User within the context of a specific Project.
 * The task is contained in the Project and is visible to whoever has visibility over its Project.
 * The Task can be marked as "completed".
 */
@Entity
public class Task {

    /**
     * Unique identifier for this Task
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Name for this task
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Description for this task
     */
    @Column
    private String description;

    /**
     * Boolean flag specifying whether this Task is completed or not
     */
    @Column(nullable = false)
    private boolean completed;
    
    /*
     * worker that work at projects
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public User worker;
    
    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
    /**
     * Timestamp for the instant this Task was created/loaded into the DB
     */
    @Column(updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

    /**
     * Timestamp for the last update of this Task into the DB
     */
    @Column(nullable = false)
    private LocalDateTime lastUpdateTimestamp;
    
    /*
     * A List of associated Tags
     */
    @ManyToMany(cascade = CascadeType.REMOVE)  //if a Task is removed remove also its Tags	 
    private List<Tag> associatedTags;


    public Task() {
    	this.associatedTags= new ArrayList<>();
    }

    public Task(String name,
                String description,
                boolean completed) {
        this.name = name;
        this.description = description;
        this.completed = completed;
    }

    /**
     * This method initializes the creationTimestamp and lastUpdateTimestamp of this User to the current instant.
     * This method is called automatically just before the User is persisted thanks to the @PrePersist annotation.
     */
    @PrePersist
    protected void onPersist() {
        this.creationTimestamp = LocalDateTime.now();
        this.lastUpdateTimestamp = LocalDateTime.now();
    }

    /**
     * This method updates the lastUpdateTimestamp of this User to the current instant.
     * This method is called automatically just before the User is updated thanks to the @PreUpdate annotation.
     */
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateTimestamp = LocalDateTime.now();
    }

    
    public void addComment(Comment comment) {
    	this.getComments().add(comment);
    }

    // GETTERS AND SETTERS

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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
    
    public List<Tag> getAssociatedTags() {
		return associatedTags;
	}

	public void setAssociatedTags(List<Tag> associatedTags) {
		this.associatedTags = associatedTags;
	}

	
	
	
	
    public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public User getWorker() {
		return worker;
	}

	public void setWorker(User worker) {
		this.worker = worker;
	}
    
	
	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return completed == task.completed &&
               Objects.equals(name, task.name) &&
                Objects.equals(creationTimestamp, task.creationTimestamp) &&
                Objects.equals(lastUpdateTimestamp, task.lastUpdateTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, completed, creationTimestamp, lastUpdateTimestamp);
    }
}

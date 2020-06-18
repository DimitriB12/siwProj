package it.uniroma3.siw.siwProj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siwProj.model.Tag;
import it.uniroma3.siw.siwProj.repository.TagRepository;

public class TagService {
	  @Autowired
	    protected TagRepository tagRepository;

	    /**
	     * This method retrieves a Tag from the DB based on its ID.
	     * @param id the id of the Tag to retrieve from the DB
	     * @return the retrieved Tag, or null if no Tag with the passed ID could be found in the DB
	     */
	    @Transactional
	    public Tag getTag(long id) {
	        Optional<Tag> result = this.tagRepository.findById(id);
	        return result.orElse(null);
	    }

	    /**
	     * This method saves a Tag in the DB.
	     * @param tag the Tag to save into the DB
	     * @return the saved Tag
	     */
	    @Transactional
	    public Tag saveTask(Tag tag) {
	        return this.tagRepository.save(tag);
	    }

	    /**
	     * This method deletes a Tag from the DB.
	     * @param tag the Tag to delete from the DB
	     */
	    @Transactional
	    public void deleteTask(Tag tag) {
	        this.tagRepository.delete(tag);
	    }
	    
	    /**
	     * This method retrieves a Tag from the DB searching the name.
	     * @param name of the Tag to retrieve from the DB
	     */
	    @Transactional
	    public Tag findTagByName(String tagName) {
	    	Optional<Tag> result = this.tagRepository.findByName(tagName);
	    	return result.orElse(null);
	    }
}

package it.uniroma3.siw.siwProj.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwProj.model.Tag;

/**
 * This interface is a CrudRepository for repository operations on Tags.
 *
 * @see Tag
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
	
	public Optional<Tag> findByName(String name);
	
	public Optional<Tag> findByColor(String color);

}

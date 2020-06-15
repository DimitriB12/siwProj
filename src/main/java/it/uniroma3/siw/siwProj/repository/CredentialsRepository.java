package it.uniroma3.siw.siwProj.repository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwProj.model.Credentials;

import java.util.Optional;

/**
 * This interface is a CrudRepository for repository operations on Credentials.
 *
 * @see Credentials
 */
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    /**
     * Retrieve Credentials by its username
     * @param username the username of the Credentials to retrieve
     * @return an Optional for the Credentials with the passed username
     */
    public Optional<Credentials> findByUserName(String username);
}




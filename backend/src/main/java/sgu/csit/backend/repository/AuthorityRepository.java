package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.AuthorityType;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityType authorityName);
}

package sgu.csit.backend.repository;

import org.springframework.data.repository.CrudRepository;
import sgu.csit.backend.web.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
}

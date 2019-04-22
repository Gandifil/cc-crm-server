package sgu.csit.backend.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.AuthorityType;
import sgu.csit.backend.model.User;
import sgu.csit.backend.repository.AuthorityRepository;
import sgu.csit.backend.repository.UserRepository;

import java.util.Collections;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {
    private AuthorityRepository authorityRepository;

    public DbInitializer(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
    }

    private void initializeAuthorities() {
        authorityRepository.deleteAll();

        Authority adminRole = new Authority(AuthorityType.ROLE_ADMIN);
        Authority userRole = new Authority(AuthorityType.ROLE_USER);

        authorityRepository.save(adminRole);
        authorityRepository.save(userRole);
    }

    @Override
    public void run(String... args) {
        System.out.println("DB initialization...");

        initializeAuthorities();

        System.out.println("Done!");
    }
}

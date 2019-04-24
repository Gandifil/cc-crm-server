package sgu.csit.backend.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.AuthorityType;
import sgu.csit.backend.model.User;
import sgu.csit.backend.repository.AuthorityRepository;
import sgu.csit.backend.repository.MetersDataRepository;
import sgu.csit.backend.repository.UserRepository;

import java.util.Collections;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {
    private AuthorityRepository authorityRepository;
    private UserRepository userRepository;
    private MetersDataRepository metersDataRepository;

    public DbInitializer(AuthorityRepository authorityRepository,
                         UserRepository userRepository,
                         MetersDataRepository metersDataRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.metersDataRepository = metersDataRepository;
    }

    private void clearDB() {
        System.out.println("Clearing DB...");
        metersDataRepository.deleteAll();
        metersDataRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
        authorityRepository.deleteAll();
        authorityRepository.flush();
        System.out.println("Done!");
    }
    private void initializeAuthorities() {
        System.out.println("Authorities initialization...");
        Authority adminRole = new Authority(AuthorityType.ROLE_ADMIN);
        Authority userRole = new Authority(AuthorityType.ROLE_USER);

        authorityRepository.save(adminRole);
        authorityRepository.save(userRole);
        System.out.println("Done!");
    }

    @Override
    public void run(String... args) {
        clearDB();
        initializeAuthorities();
    }
}

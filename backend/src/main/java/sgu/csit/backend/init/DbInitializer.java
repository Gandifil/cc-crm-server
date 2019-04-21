package sgu.csit.backend.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.AuthorityType;
import sgu.csit.backend.repository.AuthorityRepository;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {
    private AuthorityRepository authorityRepository;

    public DbInitializer(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("DB initialization...");
        authorityRepository.deleteAll();

        Authority adminRole = new Authority(AuthorityType.ROLE_ADMIN);
        Authority userRole = new Authority(AuthorityType.ROLE_USER);

        authorityRepository.save(adminRole);
        authorityRepository.save(userRole);
        System.out.println("Done!");
    }
}

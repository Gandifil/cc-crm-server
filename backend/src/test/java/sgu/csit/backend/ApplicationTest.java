package sgu.csit.backend;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.User;
import sgu.csit.backend.repository.AuthorityRepository;
import sgu.csit.backend.repository.UserRepository;
import sgu.csit.backend.service.UserService;


import static org.junit.jupiter.api.Assertions.assertEquals;
//@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @DisplayName("RegistatorTest")
    @Test
    public void registerTest() {
        System.out.println("RegistatorTest has been started!");
        assertEquals(repository.existsByUsername("myTestUser"), false);

        User user = new User();
        user.setUsername("myTestUser");
        user.setPassword("12341234");
        user.setFirstName("Максим");
        user.setSurname("Максимов");
        user.setMiddleName("Максимович");
        user.setApartment(1);
        user.setEmail("fgfdfgd@gmail.com");
        user.setPhoneNumber("8999999999999");

        userService.register(user);
        assertEquals(repository.existsByUsername("myTestUser"), true);
    }
}


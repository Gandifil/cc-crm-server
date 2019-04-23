package sgu.csit.backend.dto;

import sgu.csit.backend.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
//    private Integer apartment;
    private String phoneNumber;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.middleName = user.getMiddleName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }

    public static Set<UserDTO> toDTO(Set<User> users) {
        Set<UserDTO> userDTOs = new HashSet<>();
        for (User user : users)
            userDTOs.add(new UserDTO(user));
        return userDTOs;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}

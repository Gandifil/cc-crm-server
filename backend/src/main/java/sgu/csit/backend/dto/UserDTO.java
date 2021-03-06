package sgu.csit.backend.dto;

import sgu.csit.backend.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public static List<UserDTO> toDTO(List<User> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
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

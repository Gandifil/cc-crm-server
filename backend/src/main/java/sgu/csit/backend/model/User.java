package sgu.csit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
    private Long id;

    @Column(name = "username", length = 24, unique = true)
    @NotBlank
    @Size(min = 4, max = 24)
    private String username;

    @Column(name = "password", length = 100)
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Column(name = "first_name", length = 50)
    @Size(min = 4, max = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    @Size(min = 4, max = 50)
    private String lastName;

    @Column(name = "middle_name", length = 50)
    @Size(min = 4, max = 50)
    private String middleName;

    @Column(name = "email", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled;

    @Column(name = "last_password_reset_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @Column(name = "apartment")
    @NotNull
    private int apartment;

    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private List<Authority> authorities;

    //@JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<MetersData> metersData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSurname(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Set<MetersData> getMetersData() {
        return metersData;
    }

    public void setUsers(Set<MetersData> metersData) {
        this.metersData = metersData;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }
}
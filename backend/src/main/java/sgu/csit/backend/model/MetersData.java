package sgu.csit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "meters_data")
public class MetersData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "electricity")
    private int electricity;

    @Column(name = "hot_water")
    private int hotWater;

    @Column(name = "cold_water")
    private int coldWater;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getHotWater() {
        return hotWater;
    }

    public void setHotWater(int hotWater) {
        this.hotWater = hotWater;
    }

    public int getColdWater() {
        return coldWater;
    }

    public void setColdWater(int coldWater) {
        this.coldWater = coldWater;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

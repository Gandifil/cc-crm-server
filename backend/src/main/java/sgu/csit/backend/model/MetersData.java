package sgu.csit.backend.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "meters_data")
public class MetersData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "electricity")
    private int electricity;

    @Column(name = "hot_water")
    private int hotWater;

    @Column(name = "cold_water")
    private int coldWater;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "user_id")
    private long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

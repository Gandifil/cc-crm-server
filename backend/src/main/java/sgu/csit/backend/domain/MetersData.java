package sgu.csit.backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "metters_data")
public class MetersData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // TODO: Implement one to many
    private MetersDataType metersDataType;

    private double previousReadings;
    private double currentReadings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MetersDataType getMetersDataType() {
        return metersDataType;
    }

    public void setMetersDataType(MetersDataType metersDataType) {
        this.metersDataType = metersDataType;
    }

    public double getPreviousReadings() {
        return previousReadings;
    }

    public void setPreviousReadings(double previousReadings) {
        this.previousReadings = previousReadings;
    }

    public double getCurrentReadings() {
        return currentReadings;
    }

    public void setCurrentReadings(double currentReadings) {
        this.currentReadings = currentReadings;
    }
}

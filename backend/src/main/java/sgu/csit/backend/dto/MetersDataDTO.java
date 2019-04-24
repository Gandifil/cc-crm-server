package sgu.csit.backend.dto;

import sgu.csit.backend.model.MetersData;

import java.util.*;

public class MetersDataDTO {
    private int electricity;
    private int hotWater;
    private int coldWater;
    private Date date;

    public MetersDataDTO(MetersData metersData) {
        this.electricity = metersData.getElectricity();
        this.hotWater = metersData.getHotWater();
        this.coldWater = metersData.getColdWater();
        this.date = metersData.getDate();
    }

    public static List<MetersDataDTO> toDTO(List<MetersData> metersData) {
        List<MetersDataDTO> metersDataDTO = new ArrayList<>();
        for (MetersData md : metersData)
            metersDataDTO.add(new MetersDataDTO(md));
        return metersDataDTO;
    }

    // getters
    public int getElectricity() {
        return electricity;
    }
    public int getHotWater() {
        return hotWater;
    }
    public int getColdWater() {
        return coldWater;
    }
    public Date getDate() {
        return date;
    }
}

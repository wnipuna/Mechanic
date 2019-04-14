package com.example.smartmechanic.smart_mechanic.Model;

public class EmergencyRequest {

    private String Model;
    private String VehicleNumber;
    private String Description;
    private String Location;
    private String Image;

    public EmergencyRequest() {
    }

    public EmergencyRequest(String model, String vehicleNumber, String description, String location,String image) {
        Model = model;
        VehicleNumber = vehicleNumber;
        Description = description;
        Location = location;
        Image =image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}

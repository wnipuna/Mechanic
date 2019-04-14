package com.example.smartmechanic.smart_mechanic.Model;

public class BreakdownRequest {
    private String Model;
    private String Type;
    private String TransType;
    private String Fueltype;
    private String VehicleNumber;
    private String Description;
    private String Location;
    private String Image;

    public BreakdownRequest() {

    }

    public BreakdownRequest(String model, String type, String transType, String fueltype, String vehicleNumber, String description, String location, String image) {
        Model = model;
        Type = type;
        TransType = transType;
        Fueltype = fueltype;
        VehicleNumber = vehicleNumber;
        Description = description;
        Location = location;
        Image = image;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }

    public String getFueltype() {
        return Fueltype;
    }

    public void setFueltype(String fueltype) {
        Fueltype = fueltype;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}

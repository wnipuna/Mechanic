package com.example.smartmechanic.smart_mechanic.Model;

public class RatingRequest {

    private String Username;
    private String Password;
    private String Name;
    private String Address;
    private String Phone;
    private String IsStaff;
    private String NIC;
    private String Email;
    private String VehicleNumber;
    private String Type;

    public RatingRequest() {

    }

    public RatingRequest(String username, String password, String name, String address, String nic, String email, String vNumber, String type) {
        Username = username;
        Password = password;
        Name = name;
        Address = address;
        NIC=nic;
        IsStaff = "false";
        Email = email;
        VehicleNumber = vNumber;
        Type = type;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getType() {
        return Type;
    }
    public void setType(String type) {
         Type = type;
    }

}

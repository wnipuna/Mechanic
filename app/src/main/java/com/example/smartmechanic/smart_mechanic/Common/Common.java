package com.example.smartmechanic.smart_mechanic.Common;

import com.example.smartmechanic.smart_mechanic.Model.User;

public class Common {

    public static User currentUser;
    public static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }

}

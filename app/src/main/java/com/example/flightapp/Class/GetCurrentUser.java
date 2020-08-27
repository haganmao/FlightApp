package com.example.flightapp.Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.flightapp.Model.User;

public class GetCurrentUser {

    public static User currentUser;

    public static final String DELETE = "Delete";
    public static final String UPDATE = "Update";
    public static final String USER_KEY ="User";
    public static final String PWD_KEY ="Password";



    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Unpaid";
        else if (code.equals("1"))
            return "Pending";
        else
            return "Paid";
    }

    public static  boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i = 0; i<info.length;i++ )
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        } return false;
    }

}

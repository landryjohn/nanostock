package nanostock.model.table.generic;

import nanostock.model.table.User;

public class WatchDogApp {

    private static User userConnected ;

    public static void setUserConnected(User user ){
        userConnected = user ;
    }

    public static User getUserConnected(){
        return userConnected ;
    }

}

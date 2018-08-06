package com.manibhadra.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anil on 03-08-2018.
 */

public class AllUsersInfo implements Serializable {

    public int status;
    public String message;
    public List<UsersDataBean> usersData;

    public static class UsersDataBean implements Serializable {

        public String userId;
        public String fullName;
        public String email;
        public String http;
        public String https;
        public String profileImage;
        public String contactNumber;

    }
}

package com.manibhadra.model;

import java.util.List;

/**
 * Created by Anil on 03-08-2018.
 */

public class AllUsersInfo {

    public int status;
    public String message;
    public List<UsersDataBean> usersData;

    public static class UsersDataBean {

        public String userId;
        public String fullName;
        public String email;
        public String http;
        public String https;
        public String profileImage;
        
    }
}

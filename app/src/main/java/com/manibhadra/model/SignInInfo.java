package com.manibhadra.model;

/**
 * Created by Anil on 30-07-2018.
 */

public class SignInInfo {

    public String status;
    public String message;
    public UserDetailBean userDetail;

    public static class UserDetailBean {

        public String userId;
        public String fullName;
        public String email;
        public String password;
        public String profileImage;
        public String socialId;
        public String socialType;
        public String countryCode;
        public String contactNumber;
        public String userType;
        public String deviceType;
        public String deviceToken;
        public String authToken;
        public String status;
        public String crd;
        public String upd;
    }
}

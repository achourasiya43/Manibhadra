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
        public String birthday;
        public String gender;
        public String email;
        public String countryCode;
        public String contactNo;
        public String emailVerified;
        public String socialId;
        public String socialType;
        public String authToken;
        public String address;
        public String latitude;
        public String longitude;
        public String isProfileUpdate;
        public String mapPayment;
        public String showTopPayment;
        public String totalFriends;
        public String showOnMap = "";
        public String bankAccountStatus;
        public String profileImage;
        public String isNotification;

    }

}

package com.manju_exports.Model;

public class UserModel {
    public String status;
    public String message;
    public Data data;

    public static class Data {
        public String usname;
        public String admin_name;
        public String logintoken;
        public String ph_no;
        public String enabled;
        public String mode;

        public void setName(String name) {
            this.name = name;
        }

        /*This is for get_operation API*/
        public String name;

        /*This is for get_operator API*/
        public String barcode;
        public String role;
        public String type;
    }
}

package com.manju_exports.Model;

import java.util.ArrayList;

public class Operator_Model {

    public ArrayList<Data> data;
    public String status;
    public String message;

    public static class Data {
        public String name;
        public String id;
        public String barcode;
        public String role;
        public String type;
        public String from_time;
        public String to_time;
        public String io_no;
        public String color_code;

    }
}
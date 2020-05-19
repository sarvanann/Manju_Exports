package com.manju_exports.Model;

public class Full_App_Model_Class {
    public String status;
    public String message;
    public Data data;

    public static class Data {
        /*This is for get_bundle_data.php*/
        public String io_no;
        public String art_no;
        public String date;
        public String color_code;
        public String bundle_no;
        public String cutting_master_id;
        public String size;
        public String lot_no;
        public String tot_pcs;
    }
}


/*
{
        "data":{
        "io_no":"0001",
        "art_no":"123",
        "date":"02/21/2020",
        "color_code":"FFF",
        "bundle_no":"0001AAA",
        "cutting_master_id":"3",
        "size":"12",
        "lot_no":"857",
        "tot_pcs":24
        },
        "":"success"
        }*/

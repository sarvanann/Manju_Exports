package com.manju_exports.Model;

import java.util.ArrayList;

public class Sewing_Model {
    public String status;
    public String message;

    public ArrayList<Data> data;

    public static class Data {
        public String id,
                io_no,
                bundle_no,
                operator_id,
                operator_name,
                operation_id,
                operation_name,
                shift_id,
                shift_name,
                tot_pcs,
                finish_pcs,
                defect_pcs;
    }
}

/*
"data":[
        {
        "id":1,
        "io_no":"0001",
        "bundle_no":"0001AAA",
        "operator_id":"1",
        "operator_name":"karthi",
        "operation_id":"1",
        "operation_name":"cutting",
        "shift_id":"1",
        "shift_name":"Day Shift",
        "who_created":"1",
        "created_at":"2020-02-21 13:09:06",
        "updated_at":"2020-02-21 13:09:06"
        },*/

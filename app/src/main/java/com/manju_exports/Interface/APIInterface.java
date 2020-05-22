package com.manju_exports.Interface;

import com.manju_exports.Model.Full_App_Model_Class;
import com.manju_exports.Model.Operator_Model;
import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("login")
    Call<UserModel> SIGNUP_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("logout")
    Call<UserModel> GET_LOGOUT_DETAILS(@Header("content-type") String type, @Body String user);


    @POST("get_mistake")
    Call<Operator_Model> MISTAKES_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("get_bundle_data")
    Call<Full_App_Model_Class> GET_BUNDLE_DATA_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    /*
     *
     *This is for Sewing
     * */
    @POST("assign_sewing")
    Call<Full_App_Model_Class> GET_ASSIGN_SEWING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_sewing")
    Call<Sewing_Model> SEWING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_operator")
    Call<Operator_Model> OPERATOR_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_operation")
    Call<Operator_Model> OPERATION_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_shift")
    Call<Operator_Model> SHIFT_TIMING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    /*
     *
     *This is for Checking
     * */
    @POST("assign_checking")
    Call<Full_App_Model_Class> GET_ASSIGN_CHECKING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_checking")
    Call<Sewing_Model> CHECKING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_production_io_no")
    Call<Operator_Model> IO_NUMBER_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_production_color")
    Call<Operator_Model> COLOR_CODE_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    //Tested==>15-05-2020
    @POST("get_production_bundle")
    Call<Operator_Model> BUNDLE_NUMBER̥_CODE_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("get_mistake")
    Call<Operator_Model> MISTAKE_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    /*
     *
     *This is for Rechecking
     * */

    @POST("assign_rechecking")
    Call<Full_App_Model_Class> GET_ASSIGN_RE_CHECKING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("get_rechecking")
    Call<Sewing_Model> RE_CHECKING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);


    /*
     *
     * This is for Ironing
     *
     * */
    @POST("assign_ironing")
    Call<Full_App_Model_Class> GET_ASSIGN_IRONING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("get_ironing")
    Call<Sewing_Model> GET_IRONING_RESPONSE_CALL(@Header("content-type") String type, @Body String user);


}

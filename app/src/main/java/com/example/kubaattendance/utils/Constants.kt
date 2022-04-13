package com.example.kubaattendance.utils

interface Constants {

    companion object
    {
        var KUBA_LOGS : String = "KUBA_LOGS"
        var DOMIAN_PATH = "www.kubasystems.com"
        //http://www.kubasystems.com/api/getAllGymOwners
        //var DOMIAN_PATH = "192.168.43.44"
        var MEMBER_IMAGE_PATH : String = "http://"+DOMIAN_PATH

        //shared preferences
        var PREF_KEY : String = "pref_key"
        var CONNECTION : String = "Oops ! please check your internet connection."

        var ID : String = "id"
        var USER_ID : String = "user_id"
        var NAME : String = "name"
        var ADDRESS : String = "address"
        var PHOTO : String = "photo"
        var IS_ACTIVE : String = "is_active"
        var GYM_ID : String = "gym_id"
        var USER_NAME : String = "username"
        var PASSWORD : String = "password"
        var MOBILE : String = "mobile"
        var EMAIL : String = "password"
        var USER_TYPE : String = "usertype"
        var USER_TYPE_ID : String = "user_type_id"
        var CREATED_DATE : String = "created_date"

        var GYM_OWNER_ID : String = "gym_owner_id"
        var GYM_BRANCH_ID : String = "gym_branch_id"
        var GYM_NAME : String = "gym_name"

        var TODAY_IN_COUNT : String = "todaysInCount"
        var TODAY_OUT_COUNT : String = "todaysOutCount"
        var TODAY_TOTAL_COUNT : String = "todaysTotalCount"

        //sweet alert typles
        var SA_SUCCESS = "success"
        var SA_WARNNING = "warning"
        var SA_NORMAL = "normal"
        var SA_ERROR = "error"



    }

}
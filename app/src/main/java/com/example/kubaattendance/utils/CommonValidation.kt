package com.example.kubaattendance.utils

class CommonValidation
{
    companion object
    {
        //mobile length
        fun validateMobileLengh(mobile : String) : Boolean
        {
            if(mobile.length < 10 ||  mobile.length > 13)
            {
                T.t("Enter valid mobile")
                return false
            }
            else
                return true
        }
        //email
        fun validateEmail(email : String) : Boolean
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true
            else
            {
                T.t("Enter valid email.")
                return false
            }
        }
        //confirm password
        fun validateConfirmPassword(password : String,conf_password : String) : Boolean
        {
            if(password.equals(conf_password))
                return true
            else
            {
                T.t("Password and Confirm password should be same.")
                return false
            }

        }
    }
}
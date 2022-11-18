package com.neosoft.neostore.models


data class RegisterationModel(
    var status: String,
    var id: Int,
    var role_id: Int,
    var first_name: String,
    var last_name: String,
    var email: String,
    var username: String,
    var profile_pic: String,
    var country_id: Int,
    var gender: String,
    var phone_no: Int,
    var dob: Int,
    var is_active: String,
    var created: String,
    var modified: String,
    var access_token: String,
    var message: String,
    var user_message: String
)
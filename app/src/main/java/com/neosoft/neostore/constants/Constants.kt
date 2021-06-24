package com.neosoft.neostore.constants


class Constants {

    companion object {
        // base url
        const val BASE_URL = "http://staging.php-dev.in:8844/trainingapp/api/users/"
        const val PRODUCT_BASE_URL = "http://staging.php-dev.in:8844/trainingapp/api/products/"
        const val ADD_CART_BASE_URL = "http://staging.php-dev.in:8844/trainingapp/api/"
       //error codes
        const val SUCESS_CODE = 200
        const val Error_CODE = 500

        const val GENDER_MALE = "M"
        const val GENDER_FEMALE = "F"
        const val DELAY_TIME = 3000
        const val NOT_FOUND = 401
        const val TOKEN = "60b86b8f8203c"
        const val DATA_MISSING = 400
        const val PLACE_PICKER_REQUEST_CODE = 111
        const val CAMERA_REQUEST_CODE = 123
        const val GALLERY_REQUEST_CODE = 999
    }
}
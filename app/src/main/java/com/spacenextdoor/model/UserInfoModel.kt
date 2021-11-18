package com.spacenextdoor.model

import java.util.*

class UserInfoModel {

    var userid: Int? = null
    var customerid: Int? = null
    var providerid: Int? = null
    var firstname: String? = null
    var lastname: String? = null
    var email: String? = null
    var stripecustomerid: String? = null
    var phonenumber: String? = null
    var imageurl: String? = null
    var isemailverified: Boolean? = null
    var isphoneverified: Boolean? = null
    var facebookuserid: String? = null
    var googleuserid: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var accessToken: String? = null
    var refresh_token : String? = null
    var providerModel: SNDProviderModel? = null
    var customerModel: SNDCustomerModel? = null

}
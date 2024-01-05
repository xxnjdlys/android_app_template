package com.ymmbj.mz.model

import com.google.gson.annotations.SerializedName

class UserInfo {

    @SerializedName("user_id")
    var uid = ""

    @SerializedName("face")
    var face = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("mobile")
    var mobile = ""

    @SerializedName("name")
    var name = ""

    @SerializedName("openid")
    var openid = ""

    @SerializedName("vip_valid")
    var isVip = false

    @SerializedName("vip_valid_date")
    var vipValidDate = ""

}
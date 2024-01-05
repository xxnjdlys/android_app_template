package com.ymmbj.mz.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.ymmbj.mz.common.Constants
import com.ymmbj.mz.ktx.dataStore
import com.ymmbj.mz.ktx.getData
import com.ymmbj.mz.ktx.myApplication
import com.ymmbj.mz.ktx.putData

class InitConfig {

    companion object {

        private var mInitConfig: InitConfig? = null
        fun getInitConfig(configJson: String? = myApplication.dataStore.getData(Constants.StoreKey.INIT_CONFIG_JSON, "")): InitConfig? {
            if (mInitConfig == null) {
                mInitConfig = Gson().fromJson(configJson, InitConfig::class.java)
            }
            return mInitConfig
        }

        fun setInitConfig(initConfig: InitConfig?) {
            if (initConfig != null) {
                myApplication.dataStore.putData(Constants.StoreKey.INIT_CONFIG_JSON, Gson().toJson(initConfig))
                mInitConfig = initConfig
            }
        }

    }


    @SerializedName("global_config")
    var globalConfig = ""

}
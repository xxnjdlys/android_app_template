package com.ymmbj.mz

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.permissionx.guolindev.PermissionX
import com.ymmbj.mz.databinding.ActivityMainBinding
import com.ymmbj.mz.http.ApiClient
import com.ymmbj.mz.ktx.request
import com.ymmbj.mz.ui.activity.BaseFragmentActivity
import com.ymmbj.mz.util.Lg

class MainActivity : BaseFragmentActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?)!!
        val navController = navHostFragment.navController

        mViewBinding.navView.setupWithNavController(navController)
//        设置默认 tab
//        mViewBinding.navView.selectedItemId = R.id.nav_rank
        mViewBinding.navView.setOnItemSelectedListener { item ->
            val id = item.itemId
            Lg.d("${item.title}")

            when (id) {
                R.id.nav_puzzle -> {
                    Lg.d("nav_puzzle")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky1)
                    navController.navigate(id)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_store -> {
                    Lg.d("nav_store")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky1)
                    navController.navigate(id)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_center -> {
                    Lg.d("nav_center")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky_active1)
                    navController.navigate(id)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_gallery -> {
                    Lg.d("nav_gallery")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky1)
                    navController.navigate(id)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_rank -> {
                    navController.navigate(id)
                    Lg.d("nav_rank")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky1)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    Lg.d("nav_unknown")
                    mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky1)
                    navController.navigate(id)
                    return@setOnItemSelectedListener true
                }
            }
        }

        mViewBinding.fab.setOnClickListener {
            mViewBinding.fab.setImageResource(R.mipmap.icon_tab_lucky_active1)
            navController.navigate(R.id.nav_center)
        }

        requestPermissionIfNeeded(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA,
        )

        requestConfig()

    }

    private fun requestConfig(){
        request(
            {
                ApiClient.getInstance().getApiMethod().getInitConfig()
            }, {
                val resp = Gson().toJson(it).toString()
                Lg.d("getInitConfig successfully: $resp")
            }, {
                Lg.d("getInitConfig failure: ${it?.message}")
            }
        )
    }
    private fun requestPermissionIfNeeded(vararg permission: String) {
        PermissionX.init(this)
            .permissions(permission.toList())
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.txt_allow_manually),
                    getString(R.string.txt_allow),
                    getString(R.string.txt_deny)
                )
                Lg.i("onForwardToSettings: $permission")
            }
            .request { allGranted, grantedList, deniedList ->
                Lg.i("grantedList size: ${grantedList.size} , deniedList size: ${deniedList.size}")
                if (grantedList.size > 0) {
                    when (grantedList[0]) {
                        android.Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            Lg.i("成功获取访问位置信息 $permission is granted")
                        }
                    }
                }
            }
    }
}
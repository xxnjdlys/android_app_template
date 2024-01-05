package com.ymmbj.mz.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drakeet.multitype.MultiTypeAdapter
import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentPuzzleBinding
import com.ymmbj.mz.databinding.ItemUserEmptyBinding
import com.ymmbj.mz.databinding.ItemUserInfoBinding
import com.ymmbj.mz.ktx.AdapterData
import com.ymmbj.mz.ktx.BaseItemViewBinder
import com.ymmbj.mz.ktx.BindingVH
import com.ymmbj.mz.ktx.SingleItemViewBinder
import com.ymmbj.mz.ktx.getColorCompat
import com.ymmbj.mz.ktx.myApplication
import com.ymmbj.mz.ktx.registerMap
import com.ymmbj.mz.model.UserInfo
import com.ymmbj.mz.util.Lg
import kotlinx.coroutines.launch
import kotlin.random.Random

class PuzzleFragment : BaseFragment<FragmentPuzzleBinding>(R.layout.fragment_puzzle) {

    companion object {
        const val NAME_TYPE = 0
        const val EMAIL_TYPE = 1
    }

    private val fakeDataViewModel: FakeDataViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mViewBinding.clickProxy = ClickProxy()
        mViewBinding.textview.text = "PuzzleFragment"

        fakeDataViewModel.generate( Random.nextInt(0, 100))

        fakeDataViewModel.userList.observe(this) {
            Lg.d("fakeDataViewModel userlist size ${it?.count()}")
            val adapter = MultiTypeAdapter()
            mViewBinding.recyclerView.adapter = adapter

            if (it.isNullOrEmpty()) {
                adapter.register(UserInfo::class, EmptyItemViewBinder())
                adapter.items = mutableListOf(UserInfo())
            } else {
                val nameList = it.filter { item -> item.name.isNotEmpty() }
                val emailList = it.filter { item -> item.email.isNotEmpty() }

                adapter.registerMap(
                    mutableMapOf(
                        NAME_TYPE to ItemUserNameBinder(),
                        EMAIL_TYPE to ItemUserEmailBinder(),
                    )
                )

                val tmpNameList = java.util.ArrayList<AdapterData>()
                val tmpEmailList = java.util.ArrayList<AdapterData>()

                val unionList = java.util.ArrayList<AdapterData>()

                nameList.forEach { n ->
                    tmpNameList.add(AdapterData(NAME_TYPE, n))
                }

                emailList.forEach { e ->
                    tmpEmailList.add(AdapterData(EMAIL_TYPE, e))
                }

                // 使用zip函数进行交替插入
                val zippedList = tmpNameList.zip(tmpEmailList)
                for ((element1, element2) in zippedList) {
                    unionList.add(element1)
                    unionList.add(element2)
                }
                adapter.items = unionList
            }
        }
    }

    inner class ClickProxy {
        fun crash(): Unit = throw Exception("YMM CRASH REPORT TEST.")
    }

    inner class ItemUserNameBinder :
        BaseItemViewBinder<ItemUserInfoBinding>(R.layout.item_user_info) {
        override fun onBindViewHolder(holder: BindingVH<ItemUserInfoBinding>, item: AdapterData) {
            val data = item.data as UserInfo
            holder.viewBinding.name.text = data.name
            holder.viewBinding.container.setBackgroundColor(R.color.color_999999.getColorCompat(myApplication))
            Lg.d("name: ${data.name}")
            holder.viewBinding.executePendingBindings()
        }
    }

    inner class ItemUserEmailBinder :
        BaseItemViewBinder<ItemUserInfoBinding>(R.layout.item_user_info) {
        override fun onBindViewHolder(holder: BindingVH<ItemUserInfoBinding>, item: AdapterData) {
            val data = item.data as UserInfo
            holder.viewBinding.name.text = data.email
            holder.viewBinding.container.setBackgroundColor(R.color.color_333333.getColorCompat(myApplication))
            Lg.d("email: ${data.email}")
            holder.viewBinding.executePendingBindings()
        }
    }

    class EmptyItemViewBinder :
        SingleItemViewBinder<UserInfo, ItemUserEmptyBinding>(R.layout.item_user_empty) {
        override fun onBindViewHolder(holder: BindingVH<ItemUserEmptyBinding>, item: UserInfo) {
            holder.viewBinding.emptyImg.setImageResource(R.mipmap.ic_empty_holder)
            holder.viewBinding.emptyText.text = "暂无数据"
            Lg.d("EmptyItemViewBinder: 暂无数据")
        }
    }

    class FakeDataViewModel : ViewModel() {

        val userList = MutableLiveData<List<UserInfo>>()

        fun generate(size: Int = 10) {
            userList.postValue(ArrayList())
            if (size <= 0) {
                return
            }
            viewModelScope.launch {
                val list = ArrayList<UserInfo>()
                for (i in 1.rangeTo(size)) {
                    val user = generateRandomUserInfo()
                    list.add(user)
                }
                userList.postValue(list)
            }
        }

        private fun generateRandomUserInfo(): UserInfo {
            val random = Random
            /* 生成随机用户名 */
            val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val username = (1.rangeTo(8))
                .map { characters[random.nextInt(0, characters.length)] }
                .joinToString("")

            // 生成随机电子邮件地址
            val domains = listOf("gmail.com", "yahoo.com", "hotmail.com", "example.com")
            val email = "$username@${domains[random.nextInt(0, domains.size)]}"

            val user = UserInfo()
            user.name = username
            user.email = email
            return user
        }
    }
}
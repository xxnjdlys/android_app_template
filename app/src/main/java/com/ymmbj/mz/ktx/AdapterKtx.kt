package com.ymmbj.mz.ktx

import com.drakeet.multitype.ItemViewBinder
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.KotlinClassLinker
import com.drakeet.multitype.MultiTypeAdapter
import kotlin.reflect.KClass

fun MultiTypeAdapter.registerMap(map: Map<Int, ItemViewBinder<AdapterData, *>>) {
    register(AdapterData::class).to(*map.values.toTypedArray())
        .withKotlinClassLinker(object : KotlinClassLinker<AdapterData> {
            override fun index(position: Int, item: AdapterData): KClass<out ItemViewDelegate<AdapterData, *>> {
                return map[item.type]!!.javaClass.kotlin
            }
        })
}

data class AdapterData(val type: Int, val data: Any)
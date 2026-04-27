package com.gsm.newscircle.data.model.topheadline

import com.gsm.newscircle.data.local.entity.Source

data class ApiSource(
    val id: String,
    val name: String
)

fun ApiSource.toSourceEntity(): Source {
    return Source(
        id = id,
        name = name
    )
}
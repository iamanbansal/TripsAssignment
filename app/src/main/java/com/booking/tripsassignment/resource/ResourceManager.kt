package com.booking.tripsassignment.resource

import android.content.Context
import androidx.annotation.StringRes


class ResourceManagerImpl(private val context: Context) : ResourceManager {

    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}

interface ResourceManager {
    fun getString(@StringRes resId: Int): String
}

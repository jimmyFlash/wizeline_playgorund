package com.wizeline.bookchallenge.logic

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Emoji(var name: String, var resourceId: Int): Parcelable

package com.app.common.domain

import android.os.Parcel
import android.os.Parcelable

open class SelectInfo() :Parcelable{
    var isSelected = false
    var title:String=""
    var key:String=""

    constructor(parcel: Parcel) : this() {
        isSelected = parcel.readByte() != 0.toByte()
        title = parcel.readString()
        key = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(title)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectInfo> {
        override fun createFromParcel(parcel: Parcel): SelectInfo {
            return SelectInfo(parcel)
        }

        override fun newArray(size: Int): Array<SelectInfo?> {
            return arrayOfNulls(size)
        }
    }
}
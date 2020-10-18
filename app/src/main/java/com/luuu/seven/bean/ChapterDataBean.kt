package com.luuu.seven.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by lls on 2017/8/8.
 *
 */
data class ChapterDataBean (
        @SerializedName("chapter_id")
        var chapterId: Int,
        @SerializedName("chapter_title")
        var chapterTitle: String,
        var updatetime: Long,
        var filesize: Int,
        @SerializedName("chapter_order")
        var chapterOrder: Int
) : Parcelable {
    constructor(source: Parcel): this(source.readInt(), source.readString(), source.readLong(),
            source.readInt(), source.readInt())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(chapterId)
        p0?.writeString(chapterTitle)
        p0?.writeLong(updatetime)
        p0?.writeInt(filesize)
        p0?.writeInt(chapterOrder)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ChapterDataBean> = object : Parcelable.Creator<ChapterDataBean> {
            override fun createFromParcel(source: Parcel): ChapterDataBean{
                return ChapterDataBean(source)
            }

            override fun newArray(size: Int): Array<ChapterDataBean?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun isReadHistory(id: Int?) = chapterId == id ?: -1
}

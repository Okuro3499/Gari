package com.justin.gari.activities.onBoarding

class OnBoardItem {
    var imageID = 0
    var title: String? = null
    var description: String? = null

    fun OnBoardItem() {}

    @JvmName("getImageID1")
    fun getImageID(): Int {
        return imageID
    }

    @JvmName("setImageID1")
    fun setImageID(imageID: Int) {
        this.imageID = imageID
    }

    @JvmName("getTitle1")
    fun getTitle(): String? {
        return title
    }

    @JvmName("setTitle1")
    fun setTitle(title: String?) {
        this.title = title
    }

    @JvmName("getDescription1")
    fun getDescription(): String? {
        return description
    }

    @JvmName("setDescription1")
    fun setDescription(description: String?) {
        this.description = description
    }
}

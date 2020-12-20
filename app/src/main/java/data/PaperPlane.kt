package data

import android.view.View

class PaperPlane(
    val view: View,
    var initX: Float, var initY: Float
) {
    override fun toString(): String {
        return view.toString() + ", initX= " + initX + ", initY= " + initY
    }

    fun resetTranslationXY() {
        view.visibility = View.INVISIBLE
        view.translationX = initX
        view.translationY = initY
    }
}
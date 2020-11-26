package animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object TranslationAnim {

    private const val DEFAULT_DURATION: Long = 1000
    private const val DEFAULT_TRANSLATION_AMOUNT_S = 1100f // S: Sud
    private const val DEFAULT_TRANSLATION_AMOUNT_E = 1100f  // E: Est

    private fun translateX(view: View, amount: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "translationX",
            amount).apply {
            duration = DEFAULT_DURATION
        }
    }

    private fun translateY(view: View, amount: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "translationY",
            amount).apply {
            duration = DEFAULT_DURATION
        }
    }

    private fun translateToO(view: View): ObjectAnimator {
        return translateX(view, -DEFAULT_TRANSLATION_AMOUNT_E)
    }

    private fun translateToE(view: View): ObjectAnimator {
        return translateX(view, DEFAULT_TRANSLATION_AMOUNT_E)
    }

    private fun translateToN(view: View): ObjectAnimator {
        return translateY(view, -DEFAULT_TRANSLATION_AMOUNT_S)
    }

    private fun translateToS(view: View): ObjectAnimator {
        return translateY(view, DEFAULT_TRANSLATION_AMOUNT_S)
    }

    private fun translateToNO(view: View): AnimatorSet {
        return AnimatorSet().apply {
            play(translateToN(view)).with(translateToO(view))
        }
    }

    private fun translateToNE(view: View): AnimatorSet {
        return AnimatorSet().apply {
            play(translateToN(view)).with(translateToE(view))
        }
    }

    private fun translateToSO(view: View): AnimatorSet {
        return AnimatorSet().apply {
            play(translateToS(view)).with(translateToO(view))
        }
    }

    private fun translateToSE(view: View): AnimatorSet {
        return AnimatorSet().apply {
            play(translateToS(view)).with(translateToE(view))
        }
    }

    // PUBLIC METHODS

    /**
     * When a single direction is given, an ObjectAnimator is returned
     * When a multiple direction is given, an AnimatorSet is returned
     */
    fun translateDiagonale(view: View, dir: Direction): Animator {
        return when(dir) {
            Direction.NORD -> translateToN(view)
            Direction.SUD -> translateToS(view)
            Direction.EST -> translateToE(view)
            Direction.OUEST -> translateToO(view)
            Direction.NORDOUEST -> translateToNO(view)
            Direction.NORDEST -> translateToNE(view)
            Direction.SUDOUEST -> translateToSO(view)
            Direction.SUDEST -> translateToSE(view)
        }
    }
}

enum class Direction {
    NORD,
    SUD,
    EST,
    OUEST,
    NORDOUEST,
    NORDEST,
    SUDOUEST,
    SUDEST
}
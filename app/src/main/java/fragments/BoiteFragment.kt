package fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import animation.Direction
import animation.TranslationAnim
import data.PaperPlane
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R
import java.util.*
import kotlin.concurrent.schedule

class BoiteFragment: MyFragment() {
    private lateinit var context: MainActivity
    private lateinit var planes: Array<PaperPlane>
    private val timer = Timer("paperplane", true)
    private var planeSpecialCounter = 0
    override var TAG: String = "=====BOITEFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_boite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initPlanes()

        val pilouBtn: View = context.findViewById(R.id.buzzer_pilou)
        pilouBtn.setOnClickListener {
            context.discotheque.playRdmPilou()
        }

        val piouBtn: View = context.findViewById(R.id.buzzer_piou)
        piouBtn.setOnClickListener {
            context.discotheque.playRdmPiou()
        }

        val youpidouBtn: View = context.findViewById(R.id.buzzer_youpidou)
        youpidouBtn.setOnClickListener {
            context.discotheque.playRdmYoupidou()
        }

        val youtubeBtn: View = context.findViewById(R.id.buzzer_youtube)
        youtubeBtn.setOnClickListener {
            context.discotheque.playRdmYoutube()
        }

        val eplBtn: View = context.findViewById(R.id.buzzer_epl)
        eplBtn.setOnClickListener {
            context.discotheque.playCasteSuperieure()
            eplPaperPlaneListener()
        }

        val comuBtn: View = context.findViewById(R.id.buzzer_comu)
        comuBtn.setOnClickListener {
            context.discotheque.playCasteInferieure()
        }

        context.setMenu("buzzbox")
        context.setTitle(R.string.boite_title)
    }

    private fun initPlanes() {
        val aviondepapier = PaperPlane(context.findViewById(R.id.aviondepapier), 0f,0f)
        val aviondepapier2 = PaperPlane(context.findViewById(R.id.aviondepapier2), 0f,0f)
        val aviondepapier3 = PaperPlane(context.findViewById(R.id.aviondepapier3), 0f,0f)
        val aviondepapier4 = PaperPlane(context.findViewById(R.id.aviondepapier4), 0f,0f)
        val aviondepapier5 = PaperPlane(context.findViewById(R.id.aviondepapier5), 0f,0f)

        planes = arrayOf(aviondepapier, aviondepapier2, aviondepapier3, aviondepapier4, aviondepapier5)

        for (i in planes.indices) {
            val avion = planes.get(i)
            val translationVal = 290 + (i * 12)
            avion.view.translationX += translationVal
            avion.view.translationY += translationVal
            avion.initX = avion.view.translationX
            avion.initY = avion.view.translationY
//            Log.d(TAG, "initX: " + avion.initX + ", initY: " + avion.initY +
//                    ", x" + avion.view.x + ", y" + avion.view.y)
        }
    }

    private fun eplPaperPlaneListener() {
        playRandomPaperPlaneAnimation()
        planeSpecialCounter++
        if (planeSpecialCounter > 3) {
            playAllPaperPlanesAnimations()
        }
        timer.schedule(3000) {
            planeSpecialCounter = 0
        }
    }

    private fun playRandomPaperPlaneAnimation() {
        playPaperplaneAnimation(planes.random())
    }

    private fun playAllPaperPlanesAnimations() {
        for (i in planes.indices) {
            val plane = planes.get(i)
            plane.resetTranslationXY()
            playPaperplaneAnimation(plane)
        }
    }

    private fun playPaperplaneAnimation(aviondepapier: PaperPlane) {
        aviondepapier.view.visibility = View.VISIBLE
        //Log.d(TAG, "$aviondepapier, x" + aviondepapier.view.x + ", y" + aviondepapier.view.y)
        val rdm = Math.random()
        val flyAway: Animator
        if (rdm < 0.5) { // We send it from SE -> NO
            aviondepapier.view.rotation = 0f
            flyAway = TranslationAnim.translateDiagonale(aviondepapier.view, Direction.NORDOUEST)
        } else { // We send it from SO -> NE
            // We have to move the ship first on the other side of the screen
            aviondepapier.view.rotation = 90f
            aviondepapier.view.translationX = -1200f
            flyAway = TranslationAnim.translateDiagonale(aviondepapier.view, Direction.NORDEST)
        }
        flyAway.start()
        flyAway.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                aviondepapier.resetTranslationXY()
            }
        })
    }
}
package fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import animation.Direction
import animation.TranslationAnim
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class BoiteFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "=====BOITEFRAGMENT====="
    private var avionInitX = 0.0f
    private var avionInitY = 0.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_boite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val aviondepapier: View = context.findViewById(R.id.aviondepapier)
        aviondepapier.translationX += 300
        aviondepapier.translationY += 300
        avionInitX = aviondepapier.translationX
        avionInitY = aviondepapier.translationY
        Log.d(TAG, "X: " + avionInitX + ", Y: " + avionInitY)

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

            // Avion animation
            aviondepapier.visibility = View.VISIBLE
            val rdm = Math.random()
            val flyAway: Animator
            if (rdm < 0.1) { // We send it from SE -> NO
                aviondepapier.rotation = 0f
                flyAway = TranslationAnim.translateDiagonale(aviondepapier, Direction.NORDOUEST)
            } else { // We send it from SO -> NE
                // We have to move the ship first on the other side of the screen
                aviondepapier.rotation = 90f
                aviondepapier.translationX = -1200f
                flyAway = TranslationAnim.translateDiagonale(aviondepapier, Direction.NORDEST)
            }
            flyAway.start()
            flyAway.addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    aviondepapier.visibility = View.INVISIBLE
                    aviondepapier.translationX = avionInitX
                    aviondepapier.translationY = avionInitY
                }
            })
        }

        val comuBtn: View = context.findViewById(R.id.buzzer_comu)
        comuBtn.setOnClickListener {
            context.discotheque.playCasteInferieure()
        }

        context.setMenu("buzzbox")
        context.setTitle(R.string.boite_title)
    }
}
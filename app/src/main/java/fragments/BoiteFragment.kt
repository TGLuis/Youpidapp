package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class BoiteFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "=====BOITEFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_boite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

        context.setMenu("buzzbox")
        context.setTitle(R.string.boite_title)
    }
}
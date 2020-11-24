package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class AboutFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "=====ABOUTFRAGMENT====="
    val YTB_URLS: Array<String> =
        arrayOf(
            "https://youtu.be/miO-FRvDsfs",
            "https://youtu.be/AtCStX0RurY",
            "https://youtu.be/exGxhhHS0UE",
            "https://youtu.be/gAx8znBVux0",
            "https://youtu.be/rKFG5XcTaVk",
            "https://youtu.be/bvv2o4YuAwA",
            "https://youtu.be/rsvvCrKHSnY",
            "https://youtu.be/EkybbNppEvE",
            "https://youtu.be/LkjZFRNK9xo",
            "https://youtu.be/bxobXTOb6fM",
            "https://youtu.be/xfGsCLoN4Fg",
            "https://youtu.be/6EGAeQi474o",
            "https://youtu.be/oDJ6pZn7Y-g",
            "https://youtu.be/qVdh3T8ffIY"
        )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fabGithub: View = context.findViewById(R.id.github_link)
        fabGithub.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TGLuis/Youpidapp"))
            startActivity(browserIntent)
        }

        val fabYtb: View = context.findViewById(R.id.youtube_link)
        fabYtb.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC-QAurzK1czAlnMFOqkfxfw"))
            startActivity(browserIntent)
        }

        val covid: View = context.findViewById(R.id.covid)
        covid.setOnLongClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(YTB_URLS.random()))
            startActivity(browserIntent)
            true
        }

        context.setMenu("nothing")
    }
}
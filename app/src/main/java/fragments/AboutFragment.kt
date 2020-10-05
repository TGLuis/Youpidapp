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
    override var TAG: String = "===== ABOUTFRAGMENT ====="

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

        context.setMenu("home")
    }
}
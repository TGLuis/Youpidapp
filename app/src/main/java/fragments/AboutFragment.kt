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

        val fab: View = context.findViewById(R.id.github_link)
        fab.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TGLuis/Youpidapp"))
            startActivity(browserIntent)
        }

        context.setMenu("home")
    }
}
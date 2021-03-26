package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class ContactFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "=====CONTACTFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val contactSoundForm = context.findViewById<Button>(R.id.submit_sound_button)
        contactSoundForm.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://framaforms.org/youpidapp-soumettez-nous-votre-son-1616104740"))
            startActivity(browserIntent)
        }

        val contactBugForm = context.findViewById<Button>(R.id.submit_bug_button)
        contactBugForm.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://framaforms.org/youpidapp-rapport-de-bug-1616796324"))
            startActivity(browserIntent)
        }

        val contactFeedbackForm = context.findViewById<Button>(R.id.submit_feedback_button)
        contactFeedbackForm.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://framaforms.org/youpidapp-feedback-1616798295"))
            startActivity(browserIntent)
        }
    }

    override fun setMenu() {
        val menu = context.toolbar.menu
        menu.clear()
    }
}
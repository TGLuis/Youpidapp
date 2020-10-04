package fragments;

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R
import kotlin.random.Random

class MainFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "===== MAINFRAGMENT ====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context.title = context.getString(R.string.app_name)
        val youpidou = MediaPlayer.create(context, R.raw.youpidou)
        val pilou = MediaPlayer.create(context, R.raw.pilou)
        val monstrueusement_long = MediaPlayer.create(context, R.raw.monstrueusement_long)
        val youtube_com = MediaPlayer.create(context, R.raw.youtubepointcom)
        val all = arrayOf<MediaPlayer>(youpidou, pilou, monstrueusement_long, youtube_com)

        context.findViewById<Button>(R.id.button1)
            .setOnClickListener {youpidou.start()}

        context.findViewById<Button>(R.id.button2)
            .setOnClickListener {pilou.start()}

        context.findViewById<Button>(R.id.button3)
            .setOnClickListener {monstrueusement_long.start()}

        context.findViewById<Button>(R.id.button4)
            .setOnClickListener {youtube_com.start()}

        context.findViewById<Button>(R.id.button5)
            .setOnClickListener {
                val x = Math.abs(Random.nextInt() % all.size)
                all[x].start()
            }
    }
}

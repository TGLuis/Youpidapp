package fragments

import data.Sound
import lufra.youpidapp.Helper

class FavoritesFragment: MainFragment() {
    override fun getSounds(): List<Sound> {
        return Helper.getSounds().filter{ sound -> sound.isFavourite }
    }
}
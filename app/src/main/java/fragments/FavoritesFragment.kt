package fragments

import data.Sound
import lufra.youpidapp.DataPersistenceHelper

class FavoritesFragment: MainFragment() {
    override fun getSounds(): List<Sound> {
        return DataPersistenceHelper.getSounds().filter{ sound -> sound.isFavourite }
    }
}
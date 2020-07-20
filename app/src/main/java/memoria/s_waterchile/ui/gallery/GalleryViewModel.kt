package memoria.s_waterchile.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Aquí se mostraran los gráficos de los datos meteorológicos"
    }
    val text: LiveData<String> = _text
}
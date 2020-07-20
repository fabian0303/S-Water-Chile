package memoria.s_waterchile.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Aquí se mostraran las estadisticas de los datos meteorológicos "
    }
    val text: LiveData<String> = _text
}
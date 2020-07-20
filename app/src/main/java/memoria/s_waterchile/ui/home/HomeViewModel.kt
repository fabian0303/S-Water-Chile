package memoria.s_waterchile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Aquí se mostraran los datos meteorológicos "
    }
    val text: LiveData<String> = _text
}
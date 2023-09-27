import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastertask.Data.Habilidade
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.Status
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HabilidadeViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val skills = "habilidades"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Habilidade>> by lazy {
        MutableLiveData<List<Habilidade>>()
    }

    val getItemLiveData : MutableLiveData<Habilidade> by lazy {
        MutableLiveData<Habilidade>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(service: Service) {
        val docRef = db.collection(skills)
        docRef.add(service.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(service: Service) {
        val docRef = db.collection(skills)
        docRef.document(service.id!!).update(service.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(skills)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(skills)
        docRef.get().addOnSuccessListener {
            val skills = ArrayList<Habilidade>()
            for (item in it.documents) {
                val skill = Habilidade()
                skill.id = item.id
                skill.habilidade = item.data!!["habilidade"] as String?
                skill.preco = item.data!!["preco"] as Double?
                skills.add(skill)
            }

            getListLiveData.postValue(skills)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

    fun getItem(id: String) {
        val docRef = db.collection(skills)
        docRef.document(id).get().addOnSuccessListener {
            val skill = Habilidade()
            skill.id = it.id
            skill.habilidade = it.data!!["habilidade"] as String?
            skill.preco = it.data!!["preco"] as Double?
            getItemLiveData.postValue(skill)
        }
    }
}
package cat.copernic.meetrunning.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetrunning.R
import cat.copernic.meetrunning.databinding.FragmentHomeBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postHomeArrayList: ArrayList<PostHome>
    private lateinit var postAdapterHome: PostAdapterHome
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_home_to_addRouteMap)
        }


        postRecyclerView = binding.recycler
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.setHasFixedSize(true)

        postHomeArrayList = arrayListOf()

        postAdapterHome = PostAdapterHome(postHomeArrayList)

        postRecyclerView.adapter = postAdapterHome

        EventChangeListener()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("posts").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        postHomeArrayList.add(dc.document.toObject(PostHome::class.java))
                    }
                }
                postAdapterHome.notifyDataSetChanged()
            }
        })
    }
}

package cat.copernic.meetrunning.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetrunning.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class PostAdapterHome(private val postHomeList: ArrayList<PostHome>) :
    RecyclerView.Adapter<PostAdapterHome.MyViewHolder>() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentPost = postHomeList[position]
        val currentUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        db = FirebaseFirestore.getInstance()
        val route = currentPost

        holder.title.text = currentPost.title
        holder.ubication.text = currentPost.city

        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(HomeFragmentDirections.actionHomeToRoute(currentPost))
            Log.i("PostAdapter", "$currentPost")
        }
        //holder.image.imageAlpha = currentPost.image

        holder.shareButton.setOnClickListener {
            Log.i("PostAdapter", "clickShare")

            /*val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)*/
        }

        val exists = db.collection("users").document(currentUser).collection("favorites")
            .document(currentPost.title.toString())

        Log.i("PostAdapter1", "$exists")

        /*if(existeix){
        holder.favButton.setImageResource(R.drawable.ic_baseline_star_24check)

        holder.favButton.setOnClickListener {

            db.collection("users").document(currentUser).collection("favorites")
                .document(currentPost.title.toString()).delete(route)
            holder.favButton.setImageResource(R.drawable.ic_baseline_star_24)
        }

        } else {*/

        holder.favButton.setOnClickListener {
            db.collection("users").document(currentUser).collection("favorites")
                .document(currentPost.title.toString()).set(route)

            holder.favButton.setImageResource(R.drawable.ic_baseline_star_24check)
        }

        //}

    }

    override fun getItemCount(): Int {
        return postHomeList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.txt_title)
        val ubication: TextView = itemView.findViewById(R.id.txt_ubication)
        val shareButton: ImageButton = itemView.findViewById(R.id.shareButton)
        val favButton: ImageButton = itemView.findViewById(R.id.favButton)

        //val image : ImageView = itemView.findViewById(R.id.image_post)

    }


}
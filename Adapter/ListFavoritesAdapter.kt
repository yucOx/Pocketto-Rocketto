import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yucox.pockettorocketto.Model.Favorite
import com.yucox.pockettorocketto.View.MainActivity
import com.yucox.pockettorocketto.View.SelectActivity
import com.yucox.pockettorocketto.databinding.ListFavoriteItemBinding

class ListFavoritesAdapter(
    private val context: Context,
    private val favoritesList: MutableList<Favorite>,
    val delete: (String) -> Unit
) :
    RecyclerView.Adapter<ListFavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListFavoriteItemBinding.inflate(inflater, parent, false)
        return (ViewHolder(binding))
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoritesList[position]
        holder.binding.favNameTv.text = favorite.url

        holder.binding.removeItemBtn.setOnClickListener {
            delete(favorite.id.toHexString())
            favoritesList.remove(favorite)
            notifyItemRemoved(position)
        }

        holder.binding.bigConst.setOnClickListener {
            val intent = Intent((context as SelectActivity), MainActivity::class.java)
            intent.putExtra("url", favorite.url)
            context.startActivity(intent)
            context.finish()
        }
    }


    class ViewHolder(val binding: ListFavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
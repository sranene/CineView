package pt.ulusofona.deisi.cm2223.g22001936_22006023.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.databinding.PosterItemBinding


class PosterAdapter(private var items: List<Filme> = listOf()) : RecyclerView.Adapter<PosterAdapter.PosterFilmeViewHolder>() {

    class PosterFilmeViewHolder(val binding: PosterItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterFilmeViewHolder {

        return PosterFilmeViewHolder(
            PosterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PosterFilmeViewHolder, position: Int) { //this mf
        holder.binding.cartaz.setImageResource( items[position].cartaz)
        holder.binding.filmeName.text = items[position].nome
        holder.binding.ratingNumber.text = items[position].avaliacaoIMDB.toString()
        holder.binding.filmeAtores.text = items[position].atores
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<Filme>) {
        this.items = items
        notifyDataSetChanged()
    }


    // Implementação do HistoryAdapter

}
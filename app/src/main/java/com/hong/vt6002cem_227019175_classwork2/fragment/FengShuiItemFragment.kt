package com.hong.vt6002cem_227019175_classwork2.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.hong.vt6002cem_227019175_classwork2.R
import com.hong.vt6002cem_227019175_classwork2.databinding.FragmentFengShuiItemBinding

class FengShuiItemFragment : Fragment() {



    private lateinit var binding: FragmentFengShuiItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFengShuiItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = listOf(
            FengShuiItem(R.drawable.gold_turtle,"Feng Shui Tortoise", "This Feng Shui product is another excellent addition to any home since it represents long life. The Feng Shui Tortoise is made of resins, glass, metals, mud, crystals, and wood. It is critical to position the Feng Shui tortoises at strategic locations throughout your business or house. Following the placement rule will bring you good fortune and wealth."),
            )

        val adapter = FengShuiItemAdapter(itemList)
        binding.recyclerView.adapter = adapter
    }
    class FengShuiItemAdapter(private val itemList: List<FengShuiItem>) : RecyclerView.Adapter<FengShuiItemAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
            val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.feng_shui_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = itemList[position]
            holder.imageView.setImageResource(item.imageResId)
            holder.titleTextView.text = item.title
            holder.descriptionTextView.text = item.description
        }

        override fun getItemCount(): Int {
            return itemList.size
        }
    }

    data class FengShuiItem(val imageResId: Int, val title: String, val description: String)

}
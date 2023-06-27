package com.hong.vt6002cem_227019175_classwork2.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
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
import java.util.Locale

class FengShuiItemFragment : Fragment() {

    private fun onRecordButtonClick(view: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.startListening(intent)
    }

    val itemList = mutableListOf(
        FengShuiItem(R.drawable.lucky_bamboo,"Lucky bamboo", "You can choose the lucky bamboo as your best supporter to get good luck. Lucky Bamboo plants can be found in 2, 3, and 4 layers. It is one of the preferable Feng Shui items for luck. You can easily take care of this lucky bamboo plant, which can survive in any light. This plant can attract positive energy to your house."),
        FengShuiItem(R.drawable.gold_turtle,"Feng Shui Tortoise", "This Feng Shui product is another excellent addition to any home since it represents long life. The Feng Shui Tortoise is made of resins, glass, metals, mud, crystals, and wood. It is critical to position the Feng Shui tortoises at strategic locations throughout your business or house. Following the placement rule will bring you good fortune and wealth."),
        FengShuiItem(R.drawable.windchimes,"Windchimes","Anyone who has even a remote interest in this art knows how about Chinese Feng Shui items – wind chimes. The sweet sound of the chime when it's breezy outside can be rather calming. If you get a wind chime with metal coins, they are excellent to rectify financial losses a person might be going through."),
        FengShuiItem(R.drawable.dreamcatchers,"Dreamcatchers"
            , "Simply doing what the names suggest, dreamcatchers are Chinese Feng Shui items that keep bad dreams away. They are available in many sizes and colours and are supposed to be hung above your bed for best results."),
        FengShuiItem(R.drawable.laughing_buddha,"Laughing Buddha", "A joyful figure that will organically put a smile on your face, the spirit of the laughing Buddha creates vibrant and auspicious energy anywhere they're placed- whether it's your home or office.  "),
        FengShuiItem(R.drawable.crystal_lotus,"Crystal Lotus", "With a very strong and powerful aura, a crystal lotus is known to create romance and love in your surroundings. It is believed to bring good luck in matters of love if established in a home setting. "),
        FengShuiItem(R.drawable.three_legged_frog,"Three-legged frog", "To protect your family fortune and monies, the three-legged money frog promises a lifetime of wealth and treasure for you and your loved ones. A lot of Chinese Feng Shui items are known to bring wealth and money. However, this one is one of the most effective ones. "),
    )

    private lateinit var binding: FragmentFengShuiItemBinding
    private lateinit var speechRecognizer: SpeechRecognizer




    private val recognitionListener = object : RecognitionListener {


        override fun onReadyForSpeech(params: Bundle?) {
            // Called when the speech recognition service is ready to receive speech input
        }

        override fun onBeginningOfSpeech() {
            // Called when the user starts speaking
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Called when the volume of the user's speech changes
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // Called when the speech recognition service has received audio input
        }

        override fun onEndOfSpeech() {
            // Called when the user stops speaking
        }

        override fun onError(error: Int) {
            println(error);
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null && matches.isNotEmpty()) {
                val spokenText = matches[0]
                binding.inputEditText.setText(spokenText)

                val filteredItemList = mutableListOf<FengShuiItem>()
                for (item in itemList) {
                    if (item.title.contains(spokenText)) {
                        filteredItemList.add(item)
                    }
                }

                val adapter = FengShuiItemAdapter(filteredItemList)
                binding.recyclerView.adapter = adapter
            }
        }

        override fun onPartialResults(p0: Bundle?) {
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFengShuiItemBinding.inflate(inflater, container, false)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        binding.recordButton.setOnClickListener { onRecordButtonClick(it) }
        speechRecognizer.setRecognitionListener(recognitionListener)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FengShuiItemAdapter(itemList)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.stopListening()
        speechRecognizer.cancel()
        speechRecognizer.destroy()
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
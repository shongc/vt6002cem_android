package com.hong.vt6002cem_227019175_classwork2.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hong.vt6002cem_227019175_classwork2.R
import com.hong.vt6002cem_227019175_classwork2.databinding.FragmentHomeBinding
import com.hong.vt6002cem_227019175_classwork2.ui.home.HomeViewModel
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Declare and initialize ListView
        val listView: ListView = root.findViewById(R.id.list_view)

        val data = mutableListOf<Pair<String,String>>()



        val task = ConnectToDatabaseTask()
        val newsList = task.execute().get()

        for(news in newsList){
            data.add(Pair(news.title,news.content));
        }



        val adapter = CustomAdapter(requireContext(), data)

        // Set adapter for ListView
        listView.adapter = adapter

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    class CustomAdapter(
        context: Context,
        private val data: List<Pair<String, String>>
    ) : ArrayAdapter<Pair<String, String>>(context, 0, data) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(
                R.layout.list_item,
                parent,
                false
            )

            val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
            val contentTextView = view.findViewById<TextView>(R.id.content_text_view)

            val item = data[position]
            titleTextView.text = item.first
            contentTextView.text = item.second

            return view
        }
    }
    class ConnectToDatabaseTask : AsyncTask<Void, Void, List<News>>() {
        override fun doInBackground(vararg params: Void?): List<News>? {
            val connectionProps = Properties()
            connectionProps.put("user", "hong")
            connectionProps.put("password", "HongC_12345")
            var conn : Connection?  =null ;
            try {
                Class.forName("org.mariadb.jdbc.Driver").newInstance()
                conn = DriverManager.getConnection(
                    "jdbc:" + "mariadb" + "://" +
                            "61.239.141.245" +
                            ":" + "3306" + "/android_assignment" +
                            "",
                    connectionProps)


                val stmt = conn.createStatement()
                var newsList = mutableListOf<News>();

                val rs = stmt.executeQuery("SELECT * FROM news")
                while (rs.next()) {

                    var news = News(
                        rs.getString("title"),
                        rs.getString("content")
                    )
                    newsList.add(news);
                }
                return newsList;
            } catch (ex: SQLException) {
                // handle any errors
                ex.printStackTrace()
            } catch (ex: Exception) {
                // handle any errors
                ex.printStackTrace()
            }


            return null;

        }


    }


    class News (val title: String,val content:  String){

    }
}
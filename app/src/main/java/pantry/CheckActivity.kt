package com.pantry

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class CheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)


         fun goToWebActivity(results: ArrayList<String>, size: Int) {
            val i = Intent(this, WebActivity::class.java).also {

                // convert vector to an arraylist to use putExtra function
                val array: Array<String> = results.toArray(arrayOfNulls<String>(size))
                val list: java.util.ArrayList<String> = java.util.ArrayList()
                for (label in array) {
                    list.add(label)
                }
                it.putStringArrayListExtra("ITEM_SEARCH", list)

            }
            startActivity(i)
            finish()
        }

        //get array of stuff from HomeActivity
        var items = intent.getStringArrayListExtra("EXTRA_LABELS")

        val arrayAdapter: ArrayAdapter<*>
        val arrayList = ArrayList<String>()

        var count = 0

        //convert from ArrayList to MutableArrayList to add to ListView
        if (items != null) {
            for (label in items) {
                arrayList.add(items[count])
                count++
            }
        }

        // access the listView from xml file
        val mListView = findViewById<ListView>(R.id.list)
        //create ArrayAdapter to convert items in Array to Views in listView
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, arrayList
        )
        mListView.adapter = arrayAdapter

        var nextPage: ImageView? = null

        nextPage = findViewById(R.id.backdrop_continue)

        nextPage.setOnClickListener {
            goToWebActivity(arrayList, count)
        }

    }
}

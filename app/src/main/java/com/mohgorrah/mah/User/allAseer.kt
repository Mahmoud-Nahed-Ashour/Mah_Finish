package com.mohgorrah.mah.User

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Aseer
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_all_aseer.*
import kotlinx.android.synthetic.main.allasser_template.view.*

class allAseer : AppCompatActivity() {
    var count = 0
    private var db: FirebaseFirestore? = null

    private var adapter: FirestoreRecyclerAdapter<Aseer, ViewH>? = null

    override fun onStart() {
        super.onStart()

        db!!.collection("Aseer").get().addOnSuccessListener {
            txt.text = "عدد الاسرى الكلي : ${it.count().toString()}"

        }
        adapter!!.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_aseer)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )

        getAllAseer()

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)

    private fun getAllAseer() {
        db = Firebase.firestore

        val query = db!!.collection("Aseer").orderBy("hkm")
        val option =
            FirestoreRecyclerOptions.Builder<Aseer>().setQuery(query, Aseer::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Aseer, ViewH>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
                val i = LayoutInflater.from(this@allAseer)
                    .inflate(R.layout.allasser_template, parent, false)
                return ViewH(i)
            }

            override fun onBindViewHolder(holder: ViewH, position: Int, model: Aseer) {

                holder.itemView.name.text = "الاسم : " + model.name
                holder.itemView.location.text = "المدينة : " + model.location
                holder.itemView.hkm.text = "الحكم : " + model.hkm.toString()
                holder.itemView.date.text = "تاريخ الاعتقال : " + model.date
            }
        }

        allAseerRecycle.layoutManager = LinearLayoutManager(this)
        allAseerRecycle.adapter = adapter

    }


}
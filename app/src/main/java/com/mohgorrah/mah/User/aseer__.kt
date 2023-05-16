package com.mohgorrah.mah.User

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Aseer
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_all_aseer.*
import kotlinx.android.synthetic.main.activity_aseer.*
import kotlinx.android.synthetic.main.allasser_template.view.*
import java.util.*

class aseer__ : AppCompatActivity() {

    private var db: FirebaseFirestore? = null

    private var adapter: FirestoreRecyclerAdapter<Aseer, allAseer.ViewH>? = null

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aseer)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )


        val type = intent.getStringExtra("type").toString()
        getAseer(type)
    }


    private fun getAseer(type: String) {
        db = Firebase.firestore

        val Date = Date(2023, 3, 1)

        val query = when (type) {
            "edare" -> db!!.collection("Aseer").whereEqualTo("edare", true).orderBy("hkm")
            "male" -> db!!.collection("Aseer").whereEqualTo("male", false).orderBy("hkm")
            "moabd" -> db!!.collection("Aseer").whereEqualTo("moabd", true).orderBy("hkm")
            "20" -> db!!.collection("Aseer").whereGreaterThan("hkm", 20).whereLessThan("hkm", 35)
                .orderBy("hkm")
            "35" -> db!!.collection("Aseer").whereGreaterThanOrEqualTo("hkm", 35).orderBy("hkm")
            else -> db!!.collection("Aseer").orderBy("hkm")

        }

        val option =
            FirestoreRecyclerOptions.Builder<Aseer>().setQuery(query, Aseer::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Aseer, allAseer.ViewH>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): allAseer.ViewH {
                val i = LayoutInflater.from(this@aseer__)
                    .inflate(R.layout.allasser_template, parent, false)
                return allAseer.ViewH(i)
            }

            override fun onBindViewHolder(holder: allAseer.ViewH, position: Int, model: Aseer) {

                holder.itemView.name.text = "الاسم : " + model.name
                holder.itemView.location.text = "المدينة : " + model.location
                holder.itemView.hkm.text = "الحكم : " + model.hkm.toString()
                holder.itemView.date.text = "تاريخ الاعتقال : " + model.date

            }
        }

        AseerRecycle.layoutManager = LinearLayoutManager(this)
        AseerRecycle.adapter = adapter
    }

}
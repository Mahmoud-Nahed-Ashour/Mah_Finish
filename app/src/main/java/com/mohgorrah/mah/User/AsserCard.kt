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
import coil.api.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Aseer
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_asser_card.*
import kotlinx.android.synthetic.main.card_template.view.*

class AsserCard : AppCompatActivity() {

    private var db: FirebaseFirestore? = null

    private var adapter: FirestoreRecyclerAdapter<Aseer, ViewH>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asser_card)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        getAllAseer()

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)

    private fun getAllAseer() {
        db = Firebase.firestore

        val query = db!!.collection("Aseer")
        val option =
            FirestoreRecyclerOptions.Builder<Aseer>().setQuery(query, Aseer::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Aseer, ViewH>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
                val i = LayoutInflater.from(this@AsserCard)
                    .inflate(R.layout.card_template, parent, false)
                return ViewH(i)
            }

            override fun onBindViewHolder(holder: ViewH, position: Int, model: Aseer) {

                holder.itemView.name.text = "الاسم : " + model.name
                holder.itemView.location.text = "المدينة : " + model.location
                holder.itemView.hkm.text = "الحكم : " + model.hkm.toString()
                holder.itemView.date.text = "تاريخ الاعتقال : " + model.date
                if (model.image.isNotEmpty())
                    holder.itemView.imagee.load(model.image)

            }
        }

        cardRecycle.layoutManager = LinearLayoutManager(this)
        cardRecycle.adapter = adapter

    }


    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }


}
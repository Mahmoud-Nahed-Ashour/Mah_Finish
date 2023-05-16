package com.mohgorrah.mah.Admin

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.activity_asser.*
import kotlinx.android.synthetic.main.allasser_template.view.*

class aseer : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    private var adapter: FirestoreRecyclerAdapter<Aseer, ViewH>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asser)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        getAllAseer()


        addNewAseer.setOnClickListener {
            startActivity(Intent(this, NewAseer::class.java))
        }

    }


    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
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
                val i = LayoutInflater.from(this@aseer)
                    .inflate(R.layout.allasser_template, parent, false)
                return ViewH(i)
            }

            override fun onBindViewHolder(holder: ViewH, position: Int, model: Aseer) {

                holder.itemView.name.text = model.name
                holder.itemView.location.text = model.location
                holder.itemView.hkm.text = model.hkm.toString()
                holder.itemView.date.text = model.date

                holder.itemView.setOnLongClickListener {

                    val builder = AlertDialog.Builder(this@aseer)
                    builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete") { dialog, id ->

                            db!!.collection("Aseer").whereEqualTo("id", model.id).get()
                                .addOnSuccessListener {
                                    it.documents[0].reference.delete()
                                    Toast.makeText(this@aseer, "deleted", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                Toast.makeText(this@aseer, "Falied", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            dialog.dismiss()
                        }
                    val dialog = builder.create()
                    dialog.show()

                    return@setOnLongClickListener true
                }

            }
        }

        allAseerRecycle.layoutManager = LinearLayoutManager(this)
        allAseerRecycle.adapter = adapter

    }


}
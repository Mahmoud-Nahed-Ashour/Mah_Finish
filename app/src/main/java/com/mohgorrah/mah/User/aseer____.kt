package com.mohgorrah.mah.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Aseer
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_aseer2.*
import kotlinx.android.synthetic.main.allasser_template.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class aseer____ : AppCompatActivity() {

    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aseer2)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        db = Firebase.firestore

        when (intent.getStringExtra("type")) {
            "new" -> {
                db!!.collection("Aseer").orderBy("hkm").get()
                    .addOnSuccessListener {
                        val arra = ArrayList<Aseer>()
                        it.toObjects(Aseer::class.java).forEach { asser ->

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val dat = LocalDate.parse(asser.date, formatter)
                            if (Period.between(dat, LocalDate.now()).months <= 1)

                                arra.add(asser)
                        }

                        recycleVi.apply {
                            adapter = Adapter(this@aseer____, arra)
                            layoutManager = LinearLayoutManager(this@aseer____)
                        }
                    }
            }
            "current" -> {
                db!!.collection("Aseer").orderBy("hkm").get()
                    .addOnSuccessListener {
                        val arra = ArrayList<Aseer>()
                        it.toObjects(Aseer::class.java).forEach { asser ->

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val dat = LocalDate.parse(asser.date, formatter).plusYears(asser.hkm)

                            if (Period.between(LocalDate.now(), dat).years <= 1)
                                arra.add(asser)
                        }

                        recycleVi.apply {
                            adapter = Adapter(this@aseer____, arra)
                            layoutManager = LinearLayoutManager(this@aseer____)
                        }
                    }
            }
            else -> {/*
                db!!.collection("Aseer").get()
                    .addOnSuccessListener {
                        val arra = it.toObjects(Aseer::class.java)
                        Toast.makeText(this@aseer____, arra.size.toString(), Toast.LENGTH_SHORT)
                            .show()
                        recycleVi.apply {
                            adapter = Adapter(this@aseer____, arra)
                            layoutManager = LinearLayoutManager(this@aseer____)
                        }
                    }*/
            }
        }

    }


    class Adapter(val context: Context, val ll: ArrayList<Aseer>) :
        RecyclerView.Adapter<Adapter.viewHolder>() {

        class viewHolder(I: View) : RecyclerView.ViewHolder(I)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
            return viewHolder(
                LayoutInflater.from(context).inflate(R.layout.allasser_template, parent, false)
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: viewHolder, position: Int) {

            holder.itemView.name.text = "الاسم : " + ll[position].name
            holder.itemView.location.text = "المدينة : " + ll[position].location
            holder.itemView.hkm.text = "الحكم : " + ll[position].hkm.toString()
            holder.itemView.date.text = "تاريخ الاعتقال : " + ll[position].date


        }

        override fun getItemCount(): Int {
            return ll.size
        }


    }

}
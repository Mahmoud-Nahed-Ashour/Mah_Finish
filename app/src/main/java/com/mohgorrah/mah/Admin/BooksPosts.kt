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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.R
import com.mohgorrah.mah.User.LivresPDFActivity
import kotlinx.android.synthetic.main.activity_books_posts.*
import kotlinx.android.synthetic.main.books_template.view.*

class BooksPosts : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    private var adapter: FirestoreRecyclerAdapter<Book, ViewH>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_posts)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        addNewBook.setOnClickListener {

            startActivity(Intent(this, NewBook::class.java))
        }

        getAllBooks()


    }



    private fun getAllBooks() {
        db = Firebase.firestore

        val query = db!!.collection("Books")
        val option =
            FirestoreRecyclerOptions.Builder<Book>().setQuery(query, Book::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Book, ViewH>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
                val i = LayoutInflater.from(this@BooksPosts)
                    .inflate(R.layout.books_template, parent, false)
                return ViewH(i)
            }

            override fun onBindViewHolder(holder: ViewH, position: Int, model: Book) {

                holder.itemView.bookName.text = model.name
                holder.itemView.authorName.text = model.auth
                holder.itemView.setOnClickListener {
                    val i = Intent(this@BooksPosts, LivresPDFActivity::class.java)
                    i.putExtra("url",model.url)
                    startActivity(i)
                }

                holder.itemView.setOnLongClickListener {

                    val builder = AlertDialog.Builder(this@BooksPosts)
                    builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete") { dialog, id ->

                            Firebase.firestore.collection("Books").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                it.documents[0].reference.delete()
                                Toast.makeText(this@BooksPosts, "deleted", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this@BooksPosts, "Falied", Toast.LENGTH_SHORT).show()
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

        BooksPostsRecycle.layoutManager = GridLayoutManager(this,2)
        BooksPostsRecycle.adapter = adapter

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
}
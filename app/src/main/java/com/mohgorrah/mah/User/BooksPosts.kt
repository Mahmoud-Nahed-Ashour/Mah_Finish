package com.mohgorrah.mah.User

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Admin.Book
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_books_posts2.*
import kotlinx.android.synthetic.main.books_template.view.*

class BooksPosts : AppCompatActivity() {

    lateinit var
            booksPosts: ArrayList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_posts2)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))


        booksPosts = ArrayList()

        getBooks()

    }

    override fun onResume() {
        super.onResume()
        getBooks()
    }

    private fun getBooks() {

        Firebase.firestore.collection("Books").get()
            .addOnSuccessListener {
                val arr = it.toObjects(Book::class.java) as ArrayList
                val adapter = BooksRecycleView(this, arr)

                BooksPostsRecycle2.adapter = adapter

                BooksPostsRecycle2.layoutManager = GridLayoutManager(this, 2)
            }
    }

    class BooksRecycleView(private val cont: Context, private val List: ArrayList<Book>) :
        RecyclerView.Adapter<ViewH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
            return ViewH(
                LayoutInflater.from(cont).inflate(R.layout.books_template, parent, false)
            )

        }

        override fun onBindViewHolder(holder: ViewH, posi: Int) {
            holder.itemView.bookName.text = List[posi].name
            holder.itemView.authorName.text = List[posi].auth
            holder.itemView.setOnClickListener {
                val i = Intent(cont, LivresPDFActivity::class.java)
                i.putExtra("url",List[posi].url)
                cont.startActivity(i)
            }

        }

        override fun getItemCount(): Int {
            return List.size
        }

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)
}
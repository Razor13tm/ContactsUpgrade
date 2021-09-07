package com.example.contacts.util

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.data.Contact

class Adapter(private val onItemClicked: (id: Int) -> Unit,
    private val onItemLongClicked: (id: Int) -> Unit
) : RecyclerView.Adapter<Adapter.ContactsHolder>() {

    private val ext = AsyncListDiffer(this, DiffUtilC())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        return ContactsHolder(parent.inflate(R.layout.list_item), onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        val contact = ext.currentList[position]
        holder.assign(contact)
    }

    override fun getItemCount(): Int = ext.currentList.size

    fun updList(list: List<Contact>) {
        ext.submitList(list)
    }

    class DiffUtilC : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    class ContactsHolder(
        view: View,
        onItemClicked: (id: Int) -> Unit,
        onItemLongClicked: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.textview_name)
        private val name2: TextView = view.findViewById(R.id.textview_surname)
        private val num: TextView = view.findViewById(R.id.textview_num)
        private val minipic: ImageView = view.findViewById(R.id.imageview_thumb)
        private var mId: Int? = null

        init {
            view.setOnClickListener {
                mId?.let { onItemClicked(it.toInt()) }
            }
            view.setOnLongClickListener {
                mId?.let { onItemLongClicked(it.toInt()) }
                return@setOnLongClickListener true
            }
        }

        fun assign(contact: Contact) {
            mId = contact.id
            name.text = contact.name
            name2.text = contact.name2
            num.text = contact.num

            Glide.with(itemView)
                .load(contact.pic)
                .error(R.drawable.ic_baseline_error_24)
                .into(minipic)
        }
    }
}
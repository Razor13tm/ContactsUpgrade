package com.example.contacts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.contacts.Navigator
import com.example.contacts.R
import com.example.contacts.data.Contacts
import com.example.contacts.databinding.FragmentDetailBinding
import com.example.contacts.util.withArguments

private const val CONST = "grab"

class FragmentDetail : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding


    companion object {
        fun newInstance(id: Int): FragmentDetail {
            return FragmentDetail().withArguments {
                putInt(CONST, id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val id = requireArguments().getInt(CONST)
        initext(id)

        binding.buttonSave.setOnClickListener {
            save(id)
        }

        return binding.root
    }


    private fun save(id: Int) {
        with(Contacts.list[id]) {
            name = binding.textName.text.toString()
            name2 = binding.textSurname.text.toString()
            num = binding.textPhone.text.toString()
        }
        (activity as Navigator).navigateTo(FragmentList(), "listFragment")
    }

    private fun initext(id: Int) {
        with(binding) {
            textName.setText(Contacts.list[id].name)
            textSurname.setText(Contacts.list[id].name2)
            textPhone.setText(Contacts.list[id].num)
        }

        Glide.with(this)
            .load(Contacts.list[id].pic)
            .into(binding.imageViewDet)
    }

}
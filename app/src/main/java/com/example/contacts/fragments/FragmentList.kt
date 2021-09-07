package com.example.contacts.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.Navigator
import com.example.contacts.data.Contact
import com.example.contacts.data.Contacts
import com.example.contacts.databinding.FragmentListBinding
import com.example.contacts.util.Adapter
import com.example.contacts.util.Decorator
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator

private const val COUNT = "min"

class FragmentList : Fragment() {
    private lateinit var binding: FragmentListBinding
    private var contacts = Contacts.list
    private var adapterC: Adapter? = null
    private var alertDelete: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            contacts = savedInstanceState.getParcelableArrayList<Contact>(COUNT)
                ?: error("Unexpected state")
            init()
        } else init()


        binding.bSearch.setOnClickListener {
            searchList()
        }
        adapterC?.updList(contacts)

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(COUNT, contacts)
    }

    private fun searchList() {
        val searchName = binding.edSearch.text.toString()
        val searchSurname = binding.edSs.text.toString()

        val contz = contacts

        when {
            binding.edSearch.text.isNotEmpty() && binding.edSs.text.isNotEmpty() -> {
                contacts = contacts.filter { it.name.contains(searchName, ignoreCase = true) }
                    .filter { it.name2.contains(searchSurname, ignoreCase = true) }
                    .toMutableList() as ArrayList<Contact>

            }
            else -> {
                contacts = contacts.filter {
                    if (binding.edSearch.text.isNotEmpty()) {
                        it.name.contains(searchName, ignoreCase = true)
                    } else {
                        it.name2.contains(searchSurname, ignoreCase = true)
                    }
                }.toMutableList() as ArrayList<Contact>
            }
        }
        init()
        adapterC?.updList(contacts)
        contacts = contz
    }

    private fun navToFrDet(id: Int) {
        if (context?.resources?.configuration?.smallestScreenWidthDp!! >= 600) {
            (activity as Navigator).navigateToHor(
                FragmentDetail.newInstance(id),
                "fragmentDetail"
            )
        } else (activity as Navigator).navigateTo(FragmentDetail.newInstance(id), "fragmentDetail")

    }

    private fun delete(idItem: Int) {
        contacts = contacts.filter {
            it.id != idItem
        } as ArrayList<Contact>
        adapterC?.updList(contacts)
    }

    private fun init() {
        adapterC = Adapter({ id -> navToFrDet(id) },
            { id -> deleteDialog(id) })
        with(binding.contactList) {
            adapter = adapterC
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)

            if (itemDecorationCount == 0) {
                addItemDecoration(Decorator(requireContext()))
                itemAnimator = FlipInTopXAnimator()
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    private fun deleteDialog(id: Int) {
        alertDelete = AlertDialog.Builder(requireContext())
            .setMessage("УДАЛИТЬ")
            .setPositiveButton("OK") { _, _ -> delete(id) }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
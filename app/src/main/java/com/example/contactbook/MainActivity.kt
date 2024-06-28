package com.example.contactbook

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextContact: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var listViewContacts: ListView
    private lateinit var contacts: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextContact = findViewById(R.id.editTextContact)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonEdit = findViewById(R.id.buttonEdit)
        buttonDelete = findViewById(R.id.buttonDelete)
        listViewContacts = findViewById(R.id.listViewContacts)

        contacts = loadContacts()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
        listViewContacts.adapter = adapter

        buttonAdd.setOnClickListener {
            val contact = editTextContact.text.toString()
            if (contact.isNotEmpty()) {
                contacts.add(contact)
                adapter.notifyDataSetChanged()
                editTextContact.text.clear()
                saveContacts()
            }
        }

        buttonEdit.setOnClickListener {
            val contact = editTextContact.text.toString()
            if (selectedIndex >= 0 && contact.isNotEmpty()) {
                contacts[selectedIndex] = contact
                adapter.notifyDataSetChanged()
                editTextContact.text.clear()
                saveContacts()
            }
        }

        buttonDelete.setOnClickListener {
            if (selectedIndex >= 0) {
                contacts.removeAt(selectedIndex)
                adapter.notifyDataSetChanged()
                editTextContact.text.clear()
                selectedIndex = -1
                saveContacts()
            }
        }

        listViewContacts.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            editTextContact.setText(contacts[position])
        }
    }

    private fun loadContacts(): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("contacts", Context.MODE_PRIVATE)
        val contactsSet = sharedPreferences.getStringSet("contacts_set", setOf()) ?: setOf()
        return ArrayList(contactsSet)
    }

    private fun saveContacts() {
        val sharedPreferences = getSharedPreferences("contacts", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("contacts_set", contacts.toSet())
        editor.apply()
    }
}

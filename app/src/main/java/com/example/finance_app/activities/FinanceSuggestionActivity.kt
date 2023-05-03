package com.example.finance_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finance_app.R
import com.example.finance_app.models.SuggetionModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FinanceSuggestionActivity:AppCompatActivity() {

    private lateinit var sug_bank_name: Spinner
    private lateinit var sug_finance_type: Spinner
    private lateinit var sug_input_field: EditText
    private lateinit var sug_add_btn: Button
    private lateinit var sug_cancel_btn: Button
    private lateinit var sug_edit_btn: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_suggestions)

        sug_bank_name = findViewById(R.id.sug_bank_name)
        sug_finance_type = findViewById(R.id.sug_finance_type)
        sug_input_field = findViewById(R.id.sug_input_field)
        sug_add_btn = findViewById(R.id.sug_add_btn)
        sug_cancel_btn = findViewById(R.id.sug_cancel_btn)
        sug_edit_btn = findViewById(R.id.sug_edit_btn)

        dbRef = FirebaseDatabase.getInstance().getReference("FinSuggestions")


        sug_add_btn.setOnClickListener {
            saveSuggestionsData()
            }
        sug_cancel_btn.setOnClickListener {
            sug_input_field.text.clear()
        }
        sug_edit_btn.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }
    }


    private fun saveSuggestionsData() {



        //getting values
        val bankName = sug_bank_name.toString()
        val financeType = sug_finance_type.toString()
        val suggestion = sug_input_field.text.toString()

       /* if (bankName.isEmpty()) {
            sug_bank_name.error = "Please enter name"
            return
        }
        if (financeType.isEmpty()) {
            sug_finance_type.error = "Please enter age"
            return
        } */
        if (suggestion.isEmpty()) {
            sug_input_field.error = "Please enter the suggetion"
            return
        }

        val sugId = dbRef.push().key!!

        val suggestions = SuggetionModel(sugId, bankName, financeType, suggestion)

        dbRef.child(sugId).setValue(suggestions)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                //etEmpName.text.clear()
                //etEmpAge.text.clear()
                sug_input_field.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}
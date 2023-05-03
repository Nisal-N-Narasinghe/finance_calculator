package com.example.finance_app.activities



import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finance_app.R
import com.example.finance_app.models.SuggetionModel
import com.google.firebase.database.FirebaseDatabase


class SuggestionDetailsActivity: AppCompatActivity() {
    private lateinit var tvSugId: TextView
    private lateinit var tvBankName: TextView
    private lateinit var tvFinType: TextView
    private lateinit var tvSuggestion: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("sugId").toString(),
                intent.getStringExtra("suggetion").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("sugId").toString()
            )
        }
    }

    private fun initView() {
        tvSugId = findViewById(R.id.tvSugId)
        tvBankName = findViewById(R.id.tvBankName)
        tvFinType = findViewById(R.id.tvFinType)
        tvSuggestion = findViewById(R.id.tvSuggestion)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvSugId.text = intent.getStringExtra("sugId")
        tvBankName.text = intent.getStringExtra("bankName")
        tvFinType.text = intent.getStringExtra("finType")
        tvSuggestion.text = intent.getStringExtra("suggetion")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("FinSuggestions").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Suggestion data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingSuggestionsActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        sugId: String,
        suggetion: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_suggestion_dialog, null)

        mDialog.setView(mDialogView)

        val etBankName = mDialogView.findViewById<EditText>(R.id.etBankName)
        val etFinType = mDialogView.findViewById<EditText>(R.id.etFinType)
        val etSuggestion = mDialogView.findViewById<EditText>(R.id.etSuggestion)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etBankName.setText(intent.getStringExtra("bankName").toString())
        etFinType.setText(intent.getStringExtra("finType").toString())
        etSuggestion.setText(intent.getStringExtra("suggetion").toString())

        mDialog.setTitle("Updating $suggetion Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateSugData(
                sugId,
                etBankName.text.toString(),
                etFinType.text.toString(),
                etSuggestion.text.toString()
            )
            Toast.makeText(applicationContext, "Suggestion Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvBankName.text = etBankName.text.toString()
            tvFinType.text = etFinType.text.toString()
            tvSuggestion.text = etSuggestion.text.toString()

            alertDialog.dismiss()
        }

    }
    private fun updateSugData(
        id: String,
        bankName: String,
        bankType: String,
        suggestion: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("FinSuggestions").child(id)
        val empInfo = SuggetionModel(id, bankName, bankType, suggestion)
        dbRef.setValue(empInfo)

    }
}







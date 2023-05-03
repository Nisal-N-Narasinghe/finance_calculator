package com.example.finance_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finance_app.R
import com.example.finance_app.adapters.SugAdapter
import com.example.finance_app.models.SuggetionModel
import com.google.firebase.database.*

class FetchingSuggestionsActivity : AppCompatActivity() {

    private lateinit var sugRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var sugList: ArrayList<SuggetionModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_suggetions)

        sugRecyclerView = findViewById(R.id.rvSuggestion)
        sugRecyclerView.layoutManager = LinearLayoutManager(this)
        sugRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        sugList = arrayListOf<SuggetionModel>()

        getSuggestionData()

    }

    private fun getSuggestionData() {

        sugRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("FinSuggestions")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sugList.clear()
                if (snapshot.exists()){
                    for (sugSnap in snapshot.children){
                        val sugData = sugSnap.getValue(SuggetionModel::class.java)
                        sugList.add(sugData!!)
                    }
                    val mAdapter = SugAdapter(sugList)
                    sugRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : SugAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingSuggestionsActivity, SuggestionDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("sugId", sugList[position].sugId)
                            intent.putExtra("bankName", sugList[position].bankName)
                            intent.putExtra("finType", sugList[position].finType)
                            intent.putExtra("suggetion", sugList[position].suggetion)
                            startActivity(intent)
                        }

                    })

                    sugRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
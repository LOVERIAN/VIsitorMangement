package com.homeone.visitormanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.homeone.visitormanagement.GateKeeperAdapter
import com.homeone.visitormanagement.databinding.FragmentRequestBinding
import com.homeone.visitormanagement.modal.RequestData

class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    private var gateKeeperAdapter : GateKeeperAdapter ? = null
    private val dataModelList = ArrayList<RequestData>()
    private var linearLayoutManager : LinearLayoutManager ? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("requests")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataModelList.clear()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(RequestData::class.java)
                    dataModelList.add(data!!)
                }
                updateView()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        return binding.root
    }

    fun updateView() {
        if (dataModelList.isEmpty()) {
            binding.noReq.visibility = View.VISIBLE
        }
        gateKeeperAdapter = GateKeeperAdapter(dataModelList,context)
        linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.requestList.layoutManager = linearLayoutManager
        binding.requestList.adapter = gateKeeperAdapter
    }
}
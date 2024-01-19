package com.homeone.visitormanagement.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.homeone.visitormanagement.GateKeeperAdapter
import com.homeone.visitormanagement.adapters.AlertReqAdapter
import com.homeone.visitormanagement.databinding.AlertReqBinding
import com.homeone.visitormanagement.databinding.FragmentAlertBinding
import com.homeone.visitormanagement.modal.AlertData

class AlertFragment : Fragment() {
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    private var adapter: AlertReqAdapter? = null
    private val list = mutableListOf<AlertData>()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("soscall")
        updateView()

        myRef.child("alerts").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                if (list.isEmpty()) {
                    binding.noAlerts.visibility = View.VISIBLE
                    binding.alertList.visibility = View.GONE
                }
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                for (i in 0 until list.size) {
                    if (list[i].name == p0.key) {
                        list[i] = p0.getValue(AlertData::class.java)!!
                        adapter?.notifyDataSetChanged()
                        break
                    }
                }
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(AlertData::class.java)
                list.add(data!!)
                adapter?.notifyDataSetChanged()
                if (list.isEmpty()) {
                    binding.noAlerts.visibility = View.VISIBLE
                }else {
                    binding.noAlerts.visibility = View.INVISIBLE
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                p0.getValue(AlertData::class.java)?.let { list.remove(it) }
                adapter?.notifyDataSetChanged()
                if (list.isEmpty()) {
                    binding.noAlerts.visibility = View.VISIBLE
                }else {
                    binding.noAlerts.visibility = View.INVISIBLE
                }
            }
        })

        return binding.root
    }

    private fun updateView() {
        binding.noAlerts.visibility = View.VISIBLE
        adapter = AlertReqAdapter(list)
        binding.alertList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.alertList.adapter = adapter
    }
}
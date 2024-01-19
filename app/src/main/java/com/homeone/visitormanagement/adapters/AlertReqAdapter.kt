package com.homeone.visitormanagement.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.homeone.visitormanagement.R
import com.homeone.visitormanagement.databinding.AlertReqBinding
import com.homeone.visitormanagement.modal.AlertData


class AlertReqAdapter(private val mList: List<AlertData>) : RecyclerView.Adapter<AlertReqAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_req, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemView = mList[position]
        val message  = "Alert raised by " + itemView.name
        holder.alertMessage.text = message
        if (itemView.fire) {
            holder.fireAlert.visibility = View.VISIBLE
            holder.fireTextView.visibility = View.VISIBLE
        }else {
            holder.fireAlert.visibility = View.INVISIBLE
            holder.fireTextView.visibility = View.INVISIBLE
        }
        if (itemView.other) {
            holder.otherAlert.visibility = View.VISIBLE
            holder.otherTextView.visibility = View.VISIBLE
        } else {
            holder.otherAlert.visibility = View.INVISIBLE
            holder.otherTextView.visibility = View.INVISIBLE
        }

        holder.dismissBtn.setOnClickListener {
            val context = it.context
            val ref  = FirebaseDatabase.getInstance().getReference("soscall").child("alerts")
            ref.child(itemView.name).setValue(null).addOnSuccessListener {
                Toast.makeText(context, "Alert dismissed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val fireAlert: FloatingActionButton = itemView.findViewById(R.id.fire_alert)
        val otherAlert: FloatingActionButton = itemView.findViewById(R.id.other_alert)
        val alertMessage: TextView = itemView.findViewById(R.id.alertMessage)
        val dismissBtn: Button = itemView.findViewById(R.id.dismissBtn)
        val fireTextView : TextView = itemView.findViewById(R.id.fire_text)
        val otherTextView : TextView = itemView.findViewById(R.id.other_text)
    }
}
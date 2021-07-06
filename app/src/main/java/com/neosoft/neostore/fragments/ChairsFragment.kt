package com.neosoft.neostore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity

class ChairsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chairs, container, false)
    }
    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.getSupportActionBar()
        actionBar?.setTitle(getString(R.string.chairs))
    }
  }
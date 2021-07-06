package com.neosoft.neostore.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
import com.neosoft.neostore.activities.ResetPasswordActivity
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.EditProfileModel
import com.neosoft.neostore.models.FetchAccountModel
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.image_picker_dialog.view.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
@Suppress("DEPRECATION", "NAME_SHADOWING")
class MyAccountFragment : Fragment(), View.OnClickListener {
    //initialize variable
    lateinit var fName: String
    lateinit var lName: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var dob: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var mEmail: String
    private lateinit var mPhone: String
    private lateinit var mDob: String
    private lateinit var bitmap: Bitmap
    lateinit var loadingDialog: LoadingDialog
    private var imageString: String = "AA"
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences
    private var image=""
    val retrofit: Api = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_my_account, container, false)
        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        token = sharedPreferences.getString("token", null).toString()
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //get user details
        getUserDetails()
        iv_myaccount_profile.isEnabled = true
        btn_profile_reset_pass.setOnClickListener(this)
        iv_myaccount_profile.setOnClickListener(this)
        btn_profile_edit_profile.setOnClickListener(this)
        edt_profile_dob.setOnClickListener(this)
    }
    //api call for fetch user details
    private fun getUserDetails() {
        retrofit.fetchAccountDetails(token).enqueue(object : Callback<FetchAccountModel> {
            override fun onResponse(call: Call<FetchAccountModel>, response: Response<FetchAccountModel>) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        val handler = Handler()
                        handler.postDelayed({
                            loadingDialog.isDismiss()
                            val items = response.body()?.data?.user_data
                            fName = items?.first_name.toString()
                            lName = items?.last_name.toString()
                            email = items?.email.toString()
                            phone = items?.phone_no.toString()
                            dob = items?.dob.toString()
                            Glide.with(activity!!).load(items?.profile_pic).into(iv_myaccount_profile)
                            setUserData()
                        }, Constants.DELAY_TIME.toLong())
                    } else if (response.code() == Constants.NOT_FOUND) {
                        loadingDialog.isDismiss()
                        activity?.toast(response.body()?.user_msg.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<FetchAccountModel>, t: Throwable) {
                loadingDialog.isDismiss()
                activity?.toast(getString(R.string.no_connection))
            }
        })
    }
    //set user data from response
    private fun setUserData() {
        edt_profile_fname.setText(fName)
        edt_profile_lname.setText(lName)
        edt_profile_email.setText(email)
        edt_profile_phone.setText(phone)
        edt_profile_dob.setText(dob)
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_profile_reset_pass -> {
                val i = Intent(context, ResetPasswordActivity::class.java)
                startActivity(i)
            }
            R.id.iv_myaccount_profile -> {
                checkPermissions(android.Manifest.permission.CAMERA, Constants.GALLERY_REQUEST_CODE)
                checkPermissions(android.Manifest.permission.CAMERA, Constants.CAMERA_REQUEST_CODE)
                val view = View.inflate(activity, R.layout.image_picker_dialog, null)
                val builder = activity?.let { androidx.appcompat.app.AlertDialog.Builder(it) }
                builder?.setView(view)
                val dialog = builder?.create()
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.show()
                view.btn_camera.setOnClickListener {
                    pickFromCamera()
                    dialog?.dismiss()
                }
                view.btn_gallery.setOnClickListener {
                    pickFromGallery()
                    dialog?.dismiss()
                }
            }
            R.id.btn_profile_edit_profile -> {
                updateProfile()
            }
            R.id.edt_profile_dob -> {
                //date picker
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    requireActivity(),
                    { _: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                        edt_profile_dob.setText("" + mDay + "-" + (mMonth + 1) + "-" + mYear)
                    }, year, month, day)
                dpd.show()
            }
        }
    }
    private fun updateProfile() {
        firstName = edt_profile_fname.text.toString()
        lastName = edt_profile_lname.text.toString()
        mEmail = edt_profile_email.text.toString()
        mPhone = edt_profile_phone.text.toString()
        mDob = edt_profile_dob.text.toString()
        editProfile()
    }
    //pick image from gallery
    private fun pickFromGallery() {
        val i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        startActivityForResult(i, Constants.GALLERY_REQUEST_CODE)
    }
    //pick  camera
    private fun pickFromCamera() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i, Constants.CAMERA_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CAMERA_REQUEST_CODE) {
            if (data!=null){
                bitmap = data.extras?.get("data") as Bitmap
                uploadImage(bitmap)
                iv_myaccount_profile.setImageBitmap(bitmap)
            }
        } else if (requestCode == Constants.GALLERY_REQUEST_CODE) {
            val uri: Uri? = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                iv_myaccount_profile.setImageBitmap(bitmap)
                if (uri != null) {
                    uploadImage(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    //convert base64 image string
    private fun uploadImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)
         image = getString(R.string.profile_image_path) + imageString

    }
    //update profile api call
    private fun editProfile() {
         loadingDialog.startLoading()
        retrofit.editProfile(token, mEmail, mDob, mPhone, image, firstName, lastName)
            .enqueue(object : Callback<EditProfileModel> {
                override fun onResponse(call: Call<EditProfileModel>, response: Response<EditProfileModel>) {
                    try {
                        if (response.code() == Constants.SUCESS_CODE) {
                            val handler = Handler()
                            handler.postDelayed(
                                {
                                    loadingDialog.isDismiss()
                                    activity?.toast(response.body()?.user_msg.toString())
                                }, Constants.DELAY_TIME.toLong())
                        } else if (response.code() == Constants.Error_CODE) {
                            loadingDialog.isDismiss()
                            activity?.toast(response.body()?.user_msg.toString())
                        }
                    } catch (e: java.lang.Exception) {
                        loadingDialog.isDismiss()
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<EditProfileModel>, t: Throwable) {
                    loadingDialog.isDismiss()
                    activity?.toast(getString(R.string.no_connection))
                }
            })
    }
    //for permission check gallery and camera
    private fun checkPermissions(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            Log.d("tag",getString(R.string.permission_grant))
        }
    }
    //for permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("tag",getString(R.string.permission_grant))
            } else {
                activity?.toast(getString(R.string.permission_denied))
            }
        } else if (requestCode == Constants.GALLERY_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.toast(getString(R.string.permission_grant))
            } else {
                activity?.toast(getString(R.string.permission_denied))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = getString(R.string.my_account)
    }
}


package com.neosoft.neostore.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.ResetPasswordActivity
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.EditProfileModel
import com.neosoft.neostore.models.FetchAccountModel
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.image_picker_dialog.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class MyAccountFragment : Fragment(), View.OnClickListener {
    lateinit var fName: String
    lateinit var lName: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var dob: String
    lateinit var mfirstName:String
    lateinit var mlastName:String
    lateinit var mEmail:String
    lateinit var mphone:String
    lateinit var mdob:String
    lateinit var bitmap: Bitmap
    lateinit var encodedImage:String
     var profile:String?=""

    val retrofit = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            LayoutInflater.from(context).inflate(R.layout.fragment_my_account, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getUserDetails()
        iv_myacc_profile.isEnabled = false
        btn_profile_reset_pass.setOnClickListener(this)
        iv_myacc_profile.setOnClickListener(this)
        btn_profile_edit_profile.setOnClickListener(this)
        edt_profile_dob.setOnClickListener(this)
    }

    private fun getUserDetails() {
        retrofit.fetchAccountDetails(Constants.TOKEN).enqueue(object : Callback<FetchAccountModel> {
            override fun onResponse(
                call: Call<FetchAccountModel>,
                response: Response<FetchAccountModel>
            ) {
                try {
                    if (response.code() == Constants.SUCESS_CODE)
                    {
                        val items = response.body()?.data?.user_data
                        fName = items?.first_name.toString()
                        lName = items?.last_name.toString()
                        email = items?.email.toString()
                        phone = items?.phone_no.toString()
                        dob = items?.dob.toString()
                        //Glide.with(activity!!).load(items?.profile_pic).into(iv_myacc_profile)
                        setUserData()

                    } else if (response.code() == Constants.NOT_FOUND)
                    {
                        activity?.toast(response.body()?.user_msg.toString())
                    }

                } catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<FetchAccountModel>, t: Throwable)
            {
                activity?.toast(t.message.toString())
            }

        })
    }

    private fun setUserData() {
        edt_profile_fname.setText(fName)
        edt_profile_lname.setText(lName)
        edt_profile_email.setText(email)
        edt_profile_phone.setText(phone)
        edt_profile_dob.setText(dob)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View) {
        when(view.id)
        {
            R.id.btn_profile_reset_pass ->
            {
                val i:Intent = Intent(context,ResetPasswordActivity::class.java)
                startActivity(i)
            }

            R.id.iv_myacc_profile ->
            {
                val view = View.inflate(activity, R.layout.image_picker_dialog, null)
                val builder = activity?.let { androidx.appcompat.app.AlertDialog.Builder(it) }
                builder?.setView(view)
                val dialog = builder?.create()
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.show()
                view.btn_camera.setOnClickListener{
                    pickfromCamera()
                    dialog?.dismiss()
                }
                view.btn_gallery.setOnClickListener {
                    pickfromGallery()
                    dialog?.dismiss()
                }

            }
            R.id.btn_profile_edit_profile ->
            {
                updateProfile()
            }
            
            R.id.edt_profile_dob ->
            {
                val c= Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month =c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                
                val dpd= DatePickerDialog(requireActivity(),DatePickerDialog.OnDateSetListener{ view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                    edt_profile_dob.setText(""+mDay+"-"+(mMonth+1)+"-"+mYear)
                },year,month,day)
                dpd.show()
            }

            }
        

    }

    private fun updateProfile() {
       btn_profile_edit_profile.setText(getString(R.string.update_Profile))
        edt_profile_dob.isEnabled = true
        edt_profile_fname.isEnabled = true
        edt_profile_lname.isEnabled = true
        edt_profile_phone.isEnabled = true
        edt_profile_email.isEnabled = true
        iv_myacc_profile.isEnabled = true

         mfirstName = edt_profile_fname.text.toString()
        mlastName = edt_profile_lname.text.toString()
        mEmail = edt_profile_email.text.toString()
        mphone = edt_profile_phone.text.toString()
        mdob = edt_profile_dob.text.toString()
        editProfie()

    }



    private fun pickfromGallery() {
        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,"Gallery",999)
        val i :Intent = Intent(Intent.ACTION_PICK)
        i.type ="image/*"
        startActivityForResult(i,999)
    }

    private fun pickfromCamera() {
        requestPermission(android.Manifest.permission.CAMERA,"Camera",111)
        val i :Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111)
        {
            bitmap  = data?.extras?.get("data") as Bitmap
            iv_myacc_profile.setImageBitmap(bitmap)

        }else if(requestCode ==999)
        {
            val uri: Uri? =data?.data
            try {
                val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,uri)
                iv_myacc_profile.setImageBitmap(bitmap)
                if (uri != null) {
                    encode(uri)
                }

            }catch (e:IOException)
            {
                e.printStackTrace()
            }

        }
    }

    fun encode(imageUri: Uri): String {
        val input = activity?.getContentResolver()?.openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input , null, null)

        // Encode image to base64 string
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        profile = imageString
        return imageString
    }

/*
    private fun uploadImage(bitmap: Bitmap) {
        val byteArrayOutputStream:ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT)


    }*/

    private fun editProfie() {
            encodedImage = profile!!
        retrofit.editProfile(Constants.TOKEN,mEmail,mdob,mphone,encodedImage,mfirstName,mlastName).enqueue(object :Callback<EditProfileModel>{
            override fun onResponse(
                call: Call<EditProfileModel>,
                response: Response<EditProfileModel>
            ) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        activity?.toast(response.body()?.user_msg.toString())
                    }else if (response.code() == Constants.Error_CODE) {
                        activity?.toast(response.body()?.user_msg.toString())

                    }
                }catch (e:java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<EditProfileModel>, t: Throwable) {
                activity?.toast(t.message.toString())
            }
        })

    }


    fun requestPermission(permission: String,name: String,requestCode: Int)
    {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            when{
                activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED ->{
                    activity?.toast("$name permission granted")
                }
                shouldShowRequestPermissionRationale(permission)->showDialog(permission,name,requestCode)

                else ->ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            fun innerCheck(name: String)
            {
                if (grantResults.isEmpty() || grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    activity?.toast("$name permission refused")
                }else
                {
                    activity?.toast("$name permission granted")
                }
            }
        when(requestCode)
        {
            111->innerCheck("camera")
            999->innerCheck("gallery")
        }
    }

    fun showDialog(permission: String,name: String,requestCode: Int)
    {
        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("ok"){dialog,which ->
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode)
            }

        }
        val dialog = builder.create()
        dialog.show()
    }
}
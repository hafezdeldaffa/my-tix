package com.hafezdeldaffa.mytix.auth.signup

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hafezdeldaffa.mytix.home.HomeActivity
import com.hafezdeldaffa.mytix.R
import com.hafezdeldaffa.mytix.utils.Preferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class SignUpPhotoActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd: Boolean = false
    lateinit var filePath: Uri
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences
    private lateinit var ivAdd: ImageView
    private lateinit var ivProfile: ImageView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo)

        val tvName: TextView = findViewById(R.id.tvName)
        val btnUploadLater: Button = findViewById(R.id.btnUploadLater)
        ivAdd = findViewById(R.id.ivAddPhoto)
        ivProfile = findViewById(R.id.ivPhoto)
        btnSave = findViewById(R.id.btnSave)

        val name = intent.getStringExtra("name")
        storage = FirebaseStorage.getInstance("gs://my-tix-8b99f.appspot.com")
        storageReference = storage.reference
        preferences = Preferences(this)
        tvName.text = name

        ivAdd.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btnSave.visibility = View.VISIBLE
                ivAdd.setImageResource(R.drawable.ic_baseline_add_24)
                ivProfile.setImageResource(R.drawable.user_pic)
            } else {
                Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                    .withListener(this).check()
            }
        }

        btnUploadLater.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@SignUpPhotoActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        btnSave.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading")
                progressDialog.show()

                val storageRef = storageReference.child("images/" + UUID.randomUUID().toString())
                storageRef.putFile(filePath).addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload Success", Toast.LENGTH_LONG).show()

                    storageRef.downloadUrl.addOnSuccessListener {
                        preferences.setValues("url", it.toString())
                    }

                    finishAffinity()
                    val intent = Intent(this@SignUpPhotoActivity, HomeActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("Upload " + progress.toInt() + " %")
                }
            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager).also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "You can't upload photo now", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "You can upload photo later", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ivProfile = findViewById(R.id.ivPhoto)
        ivAdd = findViewById(R.id.ivAddPhoto)
        btnSave = findViewById(R.id.btnSave)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            Glide.with(this).load(bitmap).apply(RequestOptions.circleCropTransform())
                .into(ivProfile)

            btnSave.visibility = View.VISIBLE
            ivAdd.setImageResource(R.drawable.ic_baseline_delete)
        }
    }
}
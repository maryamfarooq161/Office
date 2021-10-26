package com.example.extract_text

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage


var image : ImageView?=null
var selectimage : Button?=null
var detectbtn : Button?=null
var textview : TextView?=null
var uri : Uri? = null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image= findViewById(R.id.image)
        selectimage= findViewById(R.id.selectimagebtn)
        detectbtn=findViewById(R.id.detectbtn)
        textview= findViewById(R.id.textfromimage)

        detectbtn!!.setOnClickListener{
            detect()
        }
    }

    private fun detect() {

        FirebaseApp.initializeApp(this);
        var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

//        var firebasevisiontextdetector : FirebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDectector()
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
//        val textRecognizer = FirebaseVision.getInstance().cloudTextRecognizer
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                textview!!.setText(firebaseVisionText.text)
            }
            .addOnFailureListener { e ->

            }
    }

    fun btnSelectImage(view: View?) {
        val PhotoPicker = Intent(Intent.ACTION_PICK)
        PhotoPicker.type = "image/*"
        startActivityForResult(PhotoPicker, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            uri = data!!.data
            image!!.setImageURI(uri)
        } else Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
    }
}
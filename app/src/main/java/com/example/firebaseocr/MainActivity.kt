package com.example.firebaseocr

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val PICK_IMAGE = 1
    var bitmap:Bitmap?=null
    var image: InputImage?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickListeners()

    }

    private fun clickListeners() {
        button.setOnClickListener {
            if(image!=null){
                val recognizer = TextRecognition.getClient()
                val result = recognizer.process(image!!)
                    .addOnSuccessListener { text ->
                        textView.text=text.text
                    }
                    .addOnFailureListener { e ->
                        textView.text=e.message
                    }
            }else{
                Toast.makeText(this, "please select image first", Toast.LENGTH_SHORT).show()
            }
        }
        imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && data!=null && data.data !=null) {
            try {
                image = InputImage.fromFilePath(this, data.data!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val imageUri: Uri = data.data!!
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            imageView.setImageBitmap(bitmap)
            
        }else{
            Toast.makeText(this, "unable to read image", Toast.LENGTH_SHORT).show()
        }
    }
}
//**
// Contains "Modified" tag [see Credit.txt for more information]
//**

package com.pantry

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class InfoActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val TAG = "PantryODT"
        const val REQUEST_IMAGE_CAPTURE: Int = 1
    }

    private lateinit var understood: ImageView
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        understood = findViewById(R.id.iv_understood)
        understood.setOnClickListener(this)

    }

    private fun goToCheckActivity(results: Vector<String>, size: Int) {
        val i = Intent(this, CheckActivity::class.java).also {

            //convert vector to an arraylist to use putExtra function
            val array: Array<String> = results.toArray(arrayOfNulls<String>(size))
            val list: ArrayList<String> = ArrayList()
            for (label in array) {
                list.add(label)
            }
            it.putStringArrayListExtra("EXTRA_LABELS", list)

        }
        startActivity(i)
        finish()
    }

    // Modified (Google):
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
            resultCode == Activity.RESULT_OK
        ) {
            beginDetection(getImage())

        }
    }

    // When Understood button is pressed, open the camera
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_understood -> {
                try {
                    openCamera()
                } catch (e: ActivityNotFoundException) {
                    Log.e(TAG, e.message.toString())
                }
            }
        }
    }

    // begin object detection
    private fun objectDetection(bitmap: Bitmap) {

        // Step 1: create TFLite's TensorImage object
        val image = TensorImage.fromBitmap(bitmap)

        // Step 2: initialize the detector object
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(25)
            .setScoreThreshold(0.2f)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(
            this, // the application context
            "custom_data5.tflite", // must be same as the filename in assets folder
            options
        )

        // Step 3: feed given image to the model and print the detection result
        val results = detector.detect(image)

        //TODO: *Create list of category labels
        compileAndSendResults(results)
    }

    // compile detection results in the labels vector and send it to CheckActivity
    private fun compileAndSendResults(results : List<Detection>) {
        val labels =  Vector<String>()
        var duplicate = false

        var count = 0

        results.map {
            // Get the top-1 category
            val category = it.categories.first()

            val newLabel = category.label

            //check for duplicate
            for (label in labels) {
                if (newLabel == label) {
                    duplicate = true
                    break
                }
            }

            //if not duplicate then add
            if (!duplicate) {
                // Add to label vector
                labels.addElement(newLabel)
                count++
            }

            duplicate = false
        }

        println("Results = $labels")

        //change activity to CheckActivty
        goToCheckActivity(results = labels, size = count)

    }

    // call object detection
    private fun beginDetection(bitmap: Bitmap) {
        // Run ODT in background thread to avoid blocking the app UI because
        lifecycleScope.launch(Dispatchers.Default) { objectDetection(bitmap) }
    }

    // decodes the captured image from camera.
    private fun getImage(): Bitmap {
        return BitmapFactory.decodeFile(currentPhotoPath)
    }

    // Modified (Google): generates a temporary image file for the Camera app to write to.
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    // Modified (Google): start the Camera app to take a photo.
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {

                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    Log.e(TAG, e.message.toString())
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {

                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.pantry",
                        it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

                }
            }
        }
    }
}


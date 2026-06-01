package com.example.apppruebauno.ui.factura

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.apppruebauno.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FacturaActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    
    private lateinit var viewFinder: PreviewView
    private lateinit var edtNumeroFactura: EditText
    private lateinit var txtNumeroDetectado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)

        viewFinder = findViewById(R.id.viewFinder)
        edtNumeroFactura = findViewById(R.id.edtNumeroFactura)
        txtNumeroDetectado = findViewById(R.id.txtNumeroDetectado)
        val btnTomarFoto = findViewById<Button>(R.id.btnTomarFoto)
        val btnGuardarFactura = findViewById<Button>(R.id.btnGuardarFactura)

        // 1. Solicitar permisos de cámara
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // 2. Configurar botón para tomar foto y procesar OCR
        btnTomarFoto.setOnClickListener {
            takePhoto()
        }

        btnGuardarFactura.setOnClickListener {
            Toast.makeText(this, "Factura guardada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Seleccionar cámara trasera por defecto
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("FacturaActivity", "Error al iniciar cámara", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    processImage(image)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("FacturaActivity", "Error al capturar imagen: ${exc.message}", exc)
                }
            }
        )
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Aquí procesamos el texto detectado
                    val fullText = visionText.text
                    Log.d("OCR", "Texto detectado: $fullText")
                    
                    // Buscamos algo que parezca un número de factura (Ejemplo: F001-000123 o similar)
                    val facturaCodigo = extraerCodigoFactura(fullText)
                    
                    if (facturaCodigo != null) {
                        edtNumeroFactura.setText(facturaCodigo)
                        txtNumeroDetectado.text = "Factura detectada: $facturaCodigo"
                        Toast.makeText(this, "Código detectado!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No se encontró un código claro", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("OCR", "Error en reconocimiento", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun extraerCodigoFactura(text: String): String? {
        // Expresión regular simple para buscar patrones tipo F001-1234567 o E001-123...
        // O simplemente buscar palabras que empiecen con F o E seguido de números
        val regex = Regex("[FE]\\d{3}-\\d+")
        val match = regex.find(text)
        return match?.value ?: if (text.contains("Factura", ignoreCase = true)) {
            // Si no hay match exacto pero hay texto, podrías intentar otra lógica
            null
        } else {
            null
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        baseContext, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

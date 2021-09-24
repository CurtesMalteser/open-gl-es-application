package com.curtesmalteser.opengles

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.curtesmalteser.opengles.databinding.ActivityMainBinding
import com.curtesmalteser.opengles.glview.MyGLSurfaceView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        gLView = MyGLSurfaceView(this)

        binding.holder.addView(gLView)

        setContentView(binding.root)

    }

}
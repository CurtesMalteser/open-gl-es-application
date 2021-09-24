package com.curtesmalteser.opengles.glview.shape

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Created by António Bastião on 24.09.21
 */
class Square(shader: (type: Int, shaderCode: String) -> Int) {

    private var mProgram: Int

    var squareCoords = floatArrayOf(
        -0.5f, 0.5f, 0.0f,      // top left
        -0.5f, -0.5f, 0.0f,      // bottom left
        0.5f, -0.5f, 0.0f,      // bottom right
        0.5f, 0.5f, 0.0f       // top right
    )

    private var mPositionHandle = 0
    private var mColorHandle = 0
    private var vPMatrixHandle = 0

    var color = floatArrayOf(0.2f, 0.709803922f, 0.898039216f, 1.0f)

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    // initialize vertex byte buffer for shape coordinates
    private var vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
    private var drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the square coordinate data
            GLES20.glVertexAttribPointer(
                it, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer
            )
        }

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also {
            // Set color for drawing the triangle
            GLES20.glUniform4fv(it, 1, color, 0)
        }

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix").also {
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(it, 1, false, mvpMatrix, 0)
        }

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer
        )

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    init {
        // initialize vertex byte buffer for shape coordinates

        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect( // (# of coordinate values * 4 bytes per float)
            squareCoords.size * 4
        ).apply {
            order(ByteOrder.nativeOrder())
        }

        vertexBuffer = bb.asFloatBuffer().apply {
            put(squareCoords)
            position(0)
        }

        // initialize byte buffer for the draw list
        drawListBuffer = ByteBuffer.allocateDirect( // (# of coordinate values * 2 bytes per short)
            drawOrder.size * 2
        ).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

        // prepare shaders and OpenGL program
        val vertexShader: Int = shader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )

        val fragmentShader: Int = shader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also { program ->

            // add the vertex shader to program
            GLES20.glAttachShader(program, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(program, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(program)
        }

    }
}
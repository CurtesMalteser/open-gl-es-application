package com.curtesmalteser.opengles.glview.shape

/**
 * Created by António Bastião on 24.09.21
 */
// Number of coordinates per vertex in triangle array
const val COORDS_PER_VERTEX = 3

// This matrix property provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
const val vertexShaderCode = "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        // Note that the uMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        "gl_Position = uMVPMatrix * vPosition;" +
        // "gl_PointSize = 40.0;" + // to increase points size
        "}"

const val fragmentShaderCode = "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "gl_FragColor = vColor;" +
        "}"
package by.drawgrid.library.model.kotlin

import by.drawgrid.library.model.Point


class Point {

    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    override fun toString(): String {
        return "x = $x; y = $y"
    }

    constructor() {
    }

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun multiplyOnScale(scale: Float) {
        x *= scale
        y *= scale
    }

    operator fun minus(point: Point) {
        this.x -= point.x
        this.x += point.x
    }
}
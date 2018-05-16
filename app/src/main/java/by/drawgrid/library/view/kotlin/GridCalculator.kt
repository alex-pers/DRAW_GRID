package by.drawgrid.library.view.kotlin

import android.util.Log
import android.view.ScaleGestureDetector
import by.drawgrid.library.model.Point
import by.drawgrid.library.myInterface.CalculatorListener


open class GridCalculator : ScaleGestureDetector.SimpleOnScaleGestureListener() {

    companion object {
        internal var MAX_SCALE = 100f
        internal var MIN_SCALE = 0.00001f
        internal var DELTA_X_Y_FOR_FINGER = 10f
    }

    var listener: CalculatorListener? = null

    var DELTA_X_Y_FOR_FINGER = 10f
    var MARGIN = 40f// make dependent dencity

    var pointMAX: Point? = null
    var pointMIN: Point? = null

    var DrawKoeficientX = 1f
    var DrawKoeficientY = 1f
    var DPI_DENSITY = 1f

    var W = 100f
    var H = 100f

    internal var flag_scroll_X = 1//
    internal var flag_scroll_Y = 1
    internal var flag_scale_X = 1
    internal var flag_scale_Y = 1

    fun calcDrawCoordinate(pointDecart: Point): Point {
        val x = DrawKoeficientX * (pointDecart.x - pointMIN?.x!!)
        val y = H - DrawKoeficientY * (pointDecart.y - pointMIN?.y!!)

        return Point(x, y)
    }

    fun calcDrawKoeficient() {
        DrawKoeficientX = W / (pointMAX?.x!! - pointMIN?.x!!)
        DrawKoeficientY = H / (pointMAX?.y!! - pointMIN?.y!!)
    }

    fun correctScaleP0(scaleLoc: Float, focusPDraw: Point) { // scale both direction

        correctScaleX(scaleLoc, focusPDraw)
        correctScaleY(scaleLoc, focusPDraw)
    }

    private fun correctScaleX(scaleLoc: Float, focusPDraw: Point) {

        Log.d("view", "DrawKoeficientX = " + DrawKoeficientX)
        //		if((grid.sizeSquareX < MAX_SCALE && scaleLoc<1 ) || (grid.sizeSquareX > MIN_SCALE && scaleLoc>1)){// ограничение по размеру квадратика
        if (1 / DrawKoeficientX < MAX_SCALE && scaleLoc < 1 || 1 / DrawKoeficientX > MIN_SCALE && scaleLoc > 1) {// ограничение по скэйлу
            val pointkoeficient = calculateKoeficientForScale(focusPDraw)

            val kx = pointkoeficient.x
            val x1 = (kx * pointMAX?.x!! + pointMIN?.x!!) / (1 + kx)

            pointMAX?.x = (pointMAX?.x!! - x1) / scaleLoc + x1
            pointMIN?.x = (pointMIN?.x!! - x1) / scaleLoc + x1
        }
    }

    private fun correctScaleY(scaleLoc: Float, focusPDraw: Point) {
        Log.d("view", "DrawKoeficientY = " + DrawKoeficientY)
        //		if((grid.sizeSquareY< MAX_SCALE && scaleLoc<1 ) || (grid.sizeSquareY > MIN_SCALE && scaleLoc>1)){
        if (1 / DrawKoeficientY < MAX_SCALE && scaleLoc < 1 || 1 / DrawKoeficientY > MIN_SCALE && scaleLoc > 1) {
            val pointkoeficient = calculateKoeficientForScale(focusPDraw)

            val ky = pointkoeficient.y
            val y1 = (ky * pointMAX?.y!! + pointMIN?.y!!) / (1 + ky)

            pointMAX?.y = (pointMAX?.y!! - y1) / scaleLoc + y1
            pointMIN?.y = (pointMIN?.y!! - y1) / scaleLoc + y1
        }

    }


    private fun calculateKoeficientForScale(px: Point): Point {
        return Point(-px.x / (px.x - W), (H - px.y) / px.y)
    }

    fun calkDotCoordinate(x: Float, y: Float): Point {
        var y = y
        y = H - y

        val x1 = x * (pointMAX?.x!! - pointMIN?.x!!) / W + pointMIN?.x!!
        val y1 = y * (pointMAX?.y!! - pointMIN?.y!!) / H + pointMIN?.y!!

        return Point(x1, y1)

    }

    fun changeMaxMin(pMAX: Point, pMIN: Point) {
        pointMAX = pMAX
        pointMIN = pMIN
        calcDrawKoeficient()
        listener?.invalidateAfterCalculating()
    }

    fun moveSystem(dx: Float, dy: Float) {

        val copyPoint = Point(pointMAX?.x!!, pointMAX?.y!!)

        pointMAX?.x = pointMAX?.x!!.minus(flag_scroll_X * dx / DrawKoeficientX)
        /** (copyPoint.x - pointMIN.x) / W */
        pointMIN?.x = pointMIN?.x!!.minus(flag_scroll_X * dx / DrawKoeficientX)
        /** (copyPoint.x - pointMIN.x) / W */
        pointMAX?.y = pointMAX?.y!!.plus(flag_scroll_Y * dy / DrawKoeficientY)
        /** (copyPoint.y - pointMIN.y) / H */
        pointMIN?.y = pointMIN?.y!!.plus(flag_scroll_Y * dy / DrawKoeficientY)
        /** (copyPoint.y - pointMIN.y) / H*/

    }

    fun fingerInHorizontalMargin(x: Float, y: Float): Boolean {// если фолс -- не попал, тру - попал

        return if (y >= H - MARGIN) true else false

    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        if (detector.isInProgress) {
            val s: Float

            s = (1.toFloat() / ((detector.previousSpan / detector.currentSpan - 1.0) * 0.5 + 1)).toFloat()// current
            // SCALE

            val point = Point(detector.focusX,
                    detector.focusY)

            if ((flag_scale_X + flag_scale_Y) == 2) {

                correctScaleP0(s, point);
            } else {
                if (flag_scale_X == 1) {
                    correctScaleX(s, point);
                }
                if (flag_scale_Y == 1) {
                    correctScaleY(s, point);
                }
            }

        }
        calcDrawKoeficient()
        listener?.invalidateAfterCalculating()
        return true
    }


}
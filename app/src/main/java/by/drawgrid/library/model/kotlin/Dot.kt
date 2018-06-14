package by.drawgrid.library.model.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import by.drawgrid.library.model.Constant
import by.drawgrid.library.model.Point
import by.drawgrid.library.view.kotlin.GridCalculator


class Dot : BaseElement {

    internal var point: Point

    var radiusCircle = 5
    internal var circleWidht = 4f
    lateinit var paintCircle: Paint
    lateinit var paintText: Paint

    constructor(context: Context?) : super(Constant.TYPE_DOT, context) {
        point = Point()
        init(dpi)
    }


    internal fun init(dpi: Float) {
        circleWidht *= dpi
        radiusCircle *= dpi.toInt()
        paintCircle = Paint()
        paintText = Paint()

        paintCircle.style = Paint.Style.FILL_AND_STROKE
        paintCircle.color = Color.GREEN
        paintCircle.strokeWidth = circleWidht

        paintText.style = Paint.Style.STROKE
        paintText.color = Color.BLACK
        paintText.textSize = 16f
    }

    override fun draw(canvas: Canvas) {


        if (pointInDrawRect(point, gridCalculator!!)) {
            val pointDraw = gridCalculator?.calcDrawCoordinate(point)
            //		float x = ViewPlusGrid.DrawKoeficientX*(point.x-ViewPlusGrid.pointMIN.x);
            //		float y = ViewPlusGrid.H-ViewPlusGrid.DrawKoeficientY*(point.y-ViewPlusGrid.pointMIN.y);
            val x = pointDraw?.x
            val y = pointDraw?.y


            canvas.drawCircle(x!!, y!!, radiusCircle.toFloat(), paintCircle)

            val strText = StringBuilder()
            strText.append(point.x).append(" , ").append(point.y)
            val label = strText.toString()

            val paint = Paint()
            val bounds = Rect()
            paint.getTextBounds(label, 0, label.length, bounds)

            bounds.width()

            canvas.drawText(label, x - bounds.width() / 2, y - (radiusCircle + 3 + bounds.height()), paintText)
        }
    }

    fun pointInDrawRect(point: Point, gridCalculator: GridCalculator): Boolean {
        return if (point.x > gridCalculator.pointMAX!!.x || point.y > gridCalculator.pointMAX!!.y || point.x < gridCalculator.pointMIN!!.x || point.y < gridCalculator.pointMIN!!.y) {
            false
        } else true

    }

    override fun getColor(): Int {
        // TODO Auto-generated method stub
        return 0
    }

    override fun setColor(color: Int) {
        // TODO Auto-generated method stub

    }

    override fun getWidthLine(): Float {
        // TODO Auto-generated method stub
        return 0f
    }


    override fun setWidthLine(widthLine: Float) {
        // TODO Auto-generated method stub

    }
}
package by.drawgrid.library.model.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import by.drawgrid.library.model.Constant
import by.drawgrid.library.model.Dot
import java.util.*


class Polygon : BaseElement {

    var dots = ArrayList<Dot>()
    lateinit var linePaint: Paint
    internal var widthLine = 2f
    internal var closedLine = false// if true -- close! false -- open

    constructor(context: Context?) : super(Constant.TYPE_POLYGON, context) {
        init()
    }

    internal fun init() {

        widthLine *= dpi
        linePaint = Paint()
        linePaint.style = Paint.Style.STROKE
        linePaint.color = Color.BLUE
        linePaint.strokeWidth = widthLine
    }

    override fun draw(canvas: Canvas) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getColor(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setColor(color: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWidthLine(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setWidthLine(widthLine: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
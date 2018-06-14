package by.drawgrid.library.model.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import by.drawgrid.library.model.Constant
import by.drawgrid.library.model.Point


class Slider : BaseElement {


    companion object {
        val ONE_SLIDER = 0
        val LEFT_SLIDER = 1
        val RIGHT_SLIDER = 2
    }


    var point = Point()
    var pointDraw = Point()

    var STATE_SLIDER = ONE_SLIDER

    internal var color: Int = 0
    lateinit var paintLine: Paint
    lateinit var paintText: Paint
    lateinit var paintCoach: Paint
    lateinit var wallpath: Path

    var marginFromGrid = 40f
    internal var lineWidth = 2f
    var coachWidth = 30f
    internal var textSize = 12f

    constructor(context: Context?) : super(Constant.TYPE_SLIDER, context) {
        init()
    }

    internal fun init() {

        lineWidth *= dpi
        coachWidth *= dpi
        textSize *= dpi
        marginFromGrid *= dpi

        paintLine = Paint()
        paintLine.style = Paint.Style.STROKE
        paintLine.color = color
        paintLine.strokeWidth = lineWidth

        paintText = Paint()
        paintText.style = Paint.Style.STROKE
        paintText.color = Color.BLUE
        paintText.textSize = textSize

        paintCoach = Paint()
        paintCoach.style = Paint.Style.FILL_AND_STROKE
        paintCoach.color = color

        wallpath = Path()
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
package by.drawgrid.library.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import by.drawgrid.library.model.*
import by.drawgrid.library.myInterface.InterfaceManagerElement
import java.util.*

/**
 *Created on 28.04.2018.
 */
class DrawGridView : View {
    companion object {
        val INVALIDATE_VIEW = 1
        val TIMER_FOR_REDRAW: Long = 30
    }

    var calculator: GridCalculator? = null
    var flag_event_for_redrow_view = true

    // for fingers
    internal var stateFinger = 0
    internal val STATE_MOVE_GRID = 1
    internal val STATE_MOVE_ELEMENT = 2
    internal val STATE_SCALE = 3
    internal val STATE_POINT = 0
    internal val STATE_NOTHING = 4
    internal var firstFingerDown = Point()
    internal var idFirstFinger: Int = 0
    private var mScaleDetector: ScaleGestureDetector? = null

    // fod draw
    private val startMaxX = 20f
    private val startMaxY = 1f
    internal var flagAutoScrollRight = false
    internal var flagAutoScrollLeft = false
    internal var flagautoScrollTop = false
    internal var flagautoScrollBottom = false
    internal var managerElement: InterfaceManagerElement = ManagerElement()
    internal var polygon /* = new Polygon()*/: Polygon? = null
    internal var grid /*= new Grid()*/: Grid? = null
    internal var sliderLeft: Slider? = null
    internal var sliderRight: Slider? = null
    internal var activeSlider = 0 // 0 оба НЕАКТИВНЫ (both unactive), 1 - активен левый (left active), 2 - активен правый(right active)

    internal var myTimer: Timer? = null
    internal var timerTask: MyTimerTask? = null


    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {

            if (flagAutoScrollRight) {
//                moveSlider(4f, activeSlider)
                invalidate()
            }

            if (flagAutoScrollLeft) {
//                moveSlider(4f, activeSlider)
                invalidate()
            }

            if (msg.what == INVALIDATE_VIEW && flag_event_for_redrow_view == true) {
                invalidate()
            }
//            setFlagForInvalidate(false)
        }
    }


    constructor(context: Context?) : super(context) {
        initView(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    fun initView(context: Context?, attrs: AttributeSet?) {
        calculator = GridCalculator()
        calculator?.pointMAX = Point(startMaxX, startMaxY)
        calculator?.pointMIN = Point(0f, 0f)

        val dm = context?.getResources()?.displayMetrics
        calculator?.DPI_DENSITY = dm?.density!!
        calculator?.MARGIN = calculator?.MARGIN!!.times(dm.density) //*= DPI_DENSITY;
        calculator?.DELTA_X_Y_FOR_FINGER = calculator?.DELTA_X_Y_FOR_FINGER!!.times(dm.density)

        mScaleDetector = ScaleGestureDetector(context, calculator)


        grid = Grid(dm?.density)
        grid?.margin = calculator?.MARGIN
        grid?.gridCalculator = calculator

        polygon = Polygon()


        addElementToManager(polygon!!)

        myTimer = Timer()
        timerTask = MyTimerTask(handler)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        myTimer?.schedule(timerTask, 0, TIMER_FOR_REDRAW)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        myTimer?.cancel()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        calculator?.W = w.toFloat()
        calculator?.H = h.toFloat()
        calculator?.calcDrawKoeficient()

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        grid?.draw(canvas)
        synchronized(managerElement) {
            managerElement.drawAllElements(canvas)
        }

        grid?.drawHorizontalRange(canvas)
        drawSliders(canvas)
        grid?.drawVerticalRange(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        setFlagForInvalidate(true)

        mScaleDetector?.onTouchEvent(event)// если два пальца, передаётся в
        // scaleListener
        val i = event.actionIndex

        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN/////////////////////////////////////////////////////////////////////////////////////////
            -> {

                idFirstFinger = event.getPointerId(i)
                firstFingerDown.x = event.x
                firstFingerDown.y = event.y


                if (calculator?.fingerInHorizontalMargin(firstFingerDown.x, firstFingerDown.y)!!) {

                    if (sliderLeft != null) {
                        if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderLeft!!)) {
                            activeSlider = 1
                            stateFinger = STATE_MOVE_ELEMENT
                            return true
                        }
                    }

                    if (sliderRight != null) {
                        if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderRight!!)) {
                            activeSlider = 2
                            stateFinger = STATE_MOVE_ELEMENT
                        }
                    }


                } else {
                    stateFinger = STATE_POINT
                }
            }

            MotionEvent.ACTION_POINTER_DOWN//////////////////////////////////////////////////////////////////////////////////
            ->

                if (event.pointerCount == 2) {
                    stateFinger = STATE_SCALE
                }

            MotionEvent.ACTION_UP////////////////////////////////////////////////////////////////////////////////////////////
            -> {
                if (stateFinger == STATE_POINT) {
                    val pointDot = calculator?.calkDotCoordinate(event.x, event.y)

                    val dot = Dot(pointDot)
                    polygon?.dots?.add(dot)
                    // points.add(dot);
                    Log.d("field",
                            "touch x:" + event.x + "  y:" + event.y
                                    + "   count:" + event.pointerCount)
                    // invalidate();

                }
                activeSlider = 0
                stateFinger = STATE_POINT
            }

            MotionEvent.ACTION_POINTER_UP///////////////////////////////////////////////////////////////////////////////////
            -> stateFinger = STATE_NOTHING

            MotionEvent.ACTION_MOVE/////////////////////////////////////////////////////////////////////////////////////////
            -> {
                Log.d("field", "move")
                when (event.pointerCount) {
                    1 -> if (stateFinger != STATE_NOTHING) {

                        if (stateFinger == STATE_MOVE_ELEMENT) {

                            if (event.getX(0) >= calculator?.W!!.minus(20 * calculator?.DPI_DENSITY!!)) {
                                flagAutoScrollRight = true
                            } else if (event.x <= calculator?.MARGIN!!.plus(5 * calculator?.DPI_DENSITY!!)) {
                                flagAutoScrollLeft = true

                            } else {
                                flagAutoScrollRight = false
                                flagAutoScrollLeft = false
                                moveSlider(event.getX(0) - firstFingerDown.x, activeSlider)
                                firstFingerDown.x = event.getX(0)
                                firstFingerDown.y = event.getY(0)
                            }

                        } else {

                            calculator?.moveSystem(event.getX(0) - firstFingerDown.x, (event.getY(0) - firstFingerDown.y))
                            firstFingerDown.x = event.getX(0)
                            firstFingerDown.y = event.getY(0)
                            calculator?.calcDrawKoeficient()
                            // invalidate();
                            stateFinger = STATE_MOVE_GRID
                        }
                    }
                }
            }
        }


        return true
    }

    fun drawSliders(canvas: Canvas) {
        if (sliderLeft != null) {
            sliderLeft?.draw(canvas)
        }
        if (sliderRight != null) {
            sliderRight?.draw(canvas)
        }
    }

    fun clearDrawFild(context: Context) {

        calculator?.MARGIN = Constant.SIZE_MARGIN
        sliderLeft = null
        sliderRight = null
        managerElement.clearAllElement()

        calculator?.changeMaxMin(Point(startMaxX, startMaxY), Point(0f, 0f))
        initView(context!!, null)
    }


    fun addElementToManager(element: Element) {
        element.gridCalculator = calculator
        managerElement.addElement(element)

    }

    internal fun setFlagForInvalidate(b: Boolean) {
        flag_event_for_redrow_view = b
    }

    private fun fingerLandOnSlides(x: Float, y: Float, slider: Slider): Boolean {// если фолс -- не попал, тру - попал

        when (slider.STATE_SLIDER) {
            Slider.ONE_SLIDER -> if (x <= slider.pointDraw.x + calculator?.DELTA_X_Y_FOR_FINGER!! + slider.coachWidth / 2 && x >= slider.pointDraw.x - calculator?.DELTA_X_Y_FOR_FINGER!! - slider.coachWidth / 2) {
                return true
            }
            Slider.LEFT_SLIDER -> if (x <= slider.pointDraw.x && x >= slider.pointDraw.x - calculator?.DELTA_X_Y_FOR_FINGER!! - slider.coachWidth / 2) {
                return true
            }
            Slider.RIGHT_SLIDER -> if (x <= slider.pointDraw.x + calculator?.DELTA_X_Y_FOR_FINGER!! + slider.coachWidth && x >= slider.pointDraw.x) {
                return true
            }
            else -> {
            }
        }

        return false
    }

    private fun moveSlider(dx: Float, activeSlider: Int) {

        when (activeSlider) {
            1 ->

                if (flagAutoScrollLeft) {
                    calculator?.moveSystem(dx, 0f)//двигаем систему
                    sliderLeft?.point = calculator?.calkDotCoordinate(sliderLeft?.pointDraw?.x!!, sliderLeft?.pointDraw?.y!!)//пересчитываем координаты слайдера

                } else if (flagAutoScrollRight) {//если автоскролл вправо

                    if (sliderLeft?.STATE_SLIDER == Slider.LEFT_SLIDER) {// если есть правый
                        if (sliderRight?.pointDraw?.x!! - sliderLeft?.pointDraw?.x!! <= dx) {//проверяем не заехал ли левый слайдер за правый
                            flagAutoScrollRight = false//запрещаем автоскролл
                        } else {
                            calculator?.moveSystem(-dx, 0f)//двигаем систему
                            sliderLeft?.point = calculator?.calkDotCoordinate(sliderLeft?.pointDraw?.x!!, sliderLeft?.pointDraw?.y!!)//пересчитываем координаты слайдера
                        }
                    } else {
                        calculator?.moveSystem(-dx, 0f)//двигаем систему
                        sliderLeft?.point = calculator?.calkDotCoordinate(sliderLeft?.pointDraw?.x!!, sliderLeft?.pointDraw?.y!!)//пересчитываем координаты слайдера

                    }


                } else {

                    if (sliderLeft?.STATE_SLIDER == Slider.ONE_SLIDER) {
                        sliderLeft?.move(dx)
                        sliderLeft?.point = calculator?.calkDotCoordinate(sliderLeft?.pointDraw?.x!!, sliderLeft?.pointDraw?.y!!)
                    } else if (sliderLeft?.STATE_SLIDER == Slider.LEFT_SLIDER) {
                        if (sliderRight?.pointDraw?.x!! - sliderLeft?.pointDraw?.x!! > dx) {

                            sliderLeft?.move(dx)
                            sliderLeft?.point = calculator?.calkDotCoordinate(sliderLeft?.pointDraw?.x!!, sliderLeft?.pointDraw?.y!!)
                        }
                    }

                }
            2 -> if (flagAutoScrollLeft) {

                if (sliderRight?.pointDraw?.x!! - sliderLeft?.pointDraw?.x!! <= -dx) {//проверяем не заехал ли правый за левый
                    flagAutoScrollLeft = false//запрещаем автоскролл
                } else {
                    calculator?.moveSystem(dx, 0f)//двигаем систему
                    sliderRight?.point = calculator?.calkDotCoordinate(sliderRight?.pointDraw?.x!!, sliderRight?.pointDraw?.y!!)//пересчитываем координаты слайдера
                }


            } else if (flagAutoScrollRight) {//если автоскролл вправо

                calculator?.moveSystem(-dx, 0f)//двигаем систему
                sliderRight?.point = calculator?.calkDotCoordinate(sliderRight?.pointDraw?.x!!, sliderRight?.pointDraw?.y!!)//пересчитываем координаты слайдера


            } else {

                if (sliderRight?.pointDraw?.x!! - sliderLeft?.pointDraw?.x!! > -dx) {
                    sliderRight?.move(dx)
                    sliderRight?.point = calculator?.calkDotCoordinate(sliderRight?.pointDraw?.x!!, sliderRight?.pointDraw?.y!!)
                }
            }

            else -> {
            }
        }

    }


    /**
     * @param x внутренней системы координат (inner system of coordinate)
     */
    fun setSlider(x: Float) {
        if (sliderLeft == null) {

            sliderLeft = Slider(x, 0f)
            sliderLeft?.STATE_SLIDER = Slider.ONE_SLIDER
            sliderLeft?.marginFromGrid = calculator?.MARGIN

        } else if (sliderRight == null) {

            if (x >= sliderLeft?.point!!.x) {
                sliderRight = Slider(x, 0f)
                sliderRight?.STATE_SLIDER = Slider.RIGHT_SLIDER
                sliderRight?.marginFromGrid = calculator?.MARGIN
                sliderLeft?.STATE_SLIDER = Slider.LEFT_SLIDER
            } else {
                sliderRight = Slider(sliderLeft?.point!!.x, 0f)
                sliderRight?.STATE_SLIDER = Slider.RIGHT_SLIDER
                sliderLeft = Slider(x, 0f)
                sliderLeft?.STATE_SLIDER = Slider.LEFT_SLIDER
                sliderRight?.marginFromGrid = calculator?.MARGIN
                sliderLeft?.marginFromGrid = calculator?.MARGIN
            }

        } else if (x >= sliderLeft?.point!!.x) {
            sliderRight = Slider(x, 0f)
            sliderRight?.STATE_SLIDER = Slider.RIGHT_SLIDER
            sliderLeft?.STATE_SLIDER = Slider.LEFT_SLIDER
        } else if (x < sliderLeft?.point!!.x) {
            sliderRight = Slider(sliderLeft?.point!!.x, 0f)
            sliderRight?.STATE_SLIDER = Slider.RIGHT_SLIDER
            sliderLeft = Slider(x, 0f)
            sliderLeft?.STATE_SLIDER = Slider.LEFT_SLIDER
        }

    }

    fun changeMaxMin(pMAX: Point, pMIN: Point) {
        calculator?.changeMaxMin(pMAX, pMIN)
        invalidate()
        //TODO: 28.04.2018 INVALIDATE
    }


    internal inner class MyTimerTask(var handlerTIMER: Handler) : TimerTask() {

        override fun run() {
            handlerTIMER.sendEmptyMessage(INVALIDATE_VIEW)

            println("")
        }
    }
}
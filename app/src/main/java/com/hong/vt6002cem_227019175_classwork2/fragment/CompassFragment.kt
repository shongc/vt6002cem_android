import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hong.vt6002cem_227019175_classwork2.R
import kotlin.math.absoluteValue

class CompassFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var compassView: CompassView? = null

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compassView = CompassView(requireContext())
        return compassView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        // Compute the rotation matrix and orientation angles
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // Convert the azimuth angle from radians to degrees
        val azimuthInRadians = orientationAngles[0]
        var azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
        azimuthInDegrees = (azimuthInDegrees + 360) % 360

        // Update the compass view with the new orientation data
        compassView?.updateData(azimuthInDegrees)
    }

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    class CompassView(context: Context) : View(context) {
        init{
            setBackgroundColor(Color.BLACK)
        }

        private val rectPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 1f
            textSize = 200f
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
        private val fontType: Typeface = Typeface.create("sans-serif",Typeface.NORMAL)
        private val textPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 1f
            textSize = 200f
            style = Paint.Style.FILL
            color = Color.WHITE
            typeface = fontType
        }

        private val linePaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 30f
            style = Paint.Style.STROKE
            color = Color.RED
        }

        private var position = 0f

        private val compassBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.compass_bg)

        override fun onDraw(canvas: Canvas) {
            val xPoint = measuredWidth / 2f
            val yPoint = measuredHeight / 2f

            val radius = measuredWidth * 0.75f / 2f

            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), rectPaint)


            val scale = (radius * 2) / Math.max(compassBitmap.width, compassBitmap.height)
            val scaledBitmap = Bitmap.createScaledBitmap(
                compassBitmap,
                (compassBitmap.width * scale).toInt(),
                (compassBitmap.height * scale).toInt(),
                true
            )
            canvas.drawBitmap(
                scaledBitmap,
                xPoint - scaledBitmap.width / 2f,
                yPoint - scaledBitmap.height / 2f,
                null
            )

            // Draw the line on top of the image and make it a little bit shorter using the linePaint object
            val lineLength = radius * 0.7f
            val endX = (xPoint + lineLength * Math.sin((-position) / 180 * Math.PI)).toFloat()
            val endY = (yPoint - lineLength * Math.cos((-position) / 180 * Math.PI)).toFloat()

            canvas.drawLine(xPoint, yPoint, endX, endY, linePaint)

            val arrowLength = 40f
            val arrowAngle = Math.toRadians(30.0)
            val angle = (-position) / 180 * Math.PI
            val arrowX1 = (endX - arrowLength * Math.sin(angle - arrowAngle)).toFloat()
            val arrowY1 = (endY + arrowLength * Math.cos(angle - arrowAngle)).toFloat()
            val arrowX2 = (endX - arrowLength * Math.sin(angle + arrowAngle)).toFloat()
            val arrowY2 = (endY + arrowLength * Math.cos(angle + arrowAngle)).toFloat()
            canvas.drawLine(endX, endY, arrowX1, arrowY1, linePaint)
            canvas.drawLine(endX, endY, arrowX2, arrowY2, linePaint)



            val textBounds = Rect()


            var currentDirection = "";

            position = (position -360).absoluteValue;
            if(position >0 && position < 90){
                currentDirection = "North-East"
            }else if(position>90 && position <180){
                currentDirection = "South-East"
            } else if( position >180 && position < 270){
                currentDirection = "South-West"
            }else if( position > 270 && position < 360){
                currentDirection = "North-West"
            }else if(position == 0f){
                currentDirection = "North";
            }else if(position == 90f){
                currentDirection = "East";
            }else if(position == 180f){
                currentDirection = "South";
            }else if(position == 270f){
                currentDirection = "West";
            }
            var displayDirectionString = currentDirection
            textPaint.getTextBounds(displayDirectionString, 0, displayDirectionString.length, textBounds)

            val x = (measuredWidth - textBounds.width()) / 2f - 20f
            val y = measuredHeight - textBounds.height() - 100f
            canvas.drawText(displayDirectionString , x, y, textPaint)

            canvas.drawText(position.toInt().toString()+"° ", x, measuredHeight - 50f, textPaint)
        }

        fun updateData(position: Float) {
            this.position = position
            invalidate()
        }
    }

}



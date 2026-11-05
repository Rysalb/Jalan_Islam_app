package com.jalanislam.app.data.sensor

import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.jalanislam.app.data.model.QiblaInfo
import com.jalanislam.app.data.prayer.AstronomicalPrayerCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

class CompassSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var currentLatitude: Double = -6.1818 // Default Jakarta
    private var currentLongitude: Double = 106.8223
    private var qiblaBearing: Float = 295.15f
    private var distanceToKabahKm: Double = 7916.0
    private var smoothedHeading: Float = 0f

    private val _qiblaInfoFlow = MutableStateFlow(
        QiblaInfo(
            qiblaBearing = qiblaBearing,
            deviceHeading = 0f,
            qiblaRelativeAngle = qiblaBearing,
            distanceToKabahKm = distanceToKabahKm,
            sensorAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH
        )
    )
    val qiblaInfoFlow: StateFlow<QiblaInfo> = _qiblaInfoFlow.asStateFlow()

    fun updateCoordinates(latitude: Double, longitude: Double) {
        currentLatitude = latitude
        currentLongitude = longitude
        qiblaBearing = AstronomicalPrayerCalculator.calculateQiblaBearing(latitude, longitude)
        distanceToKabahKm = AstronomicalPrayerCalculator.calculateDistanceToKabahKm(latitude, longitude)
        emitCurrentState(smoothedHeading, _qiblaInfoFlow.value.sensorAccuracy)
    }

    fun startListening() {
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            magneticSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        var rawHeading = 0f

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            rawHeading = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            updateOrientationFromSensors()
            return
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            updateOrientationFromSensors()
            return
        }

        processHeading(rawHeading, event.accuracy)
    }

    private fun updateOrientationFromSensors() {
        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val rawHeading = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            processHeading(rawHeading, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM)
        }
    }

    private fun processHeading(rawHeadingDeg: Float, accuracy: Int) {
        // Normalize 0..360
        var heading = (rawHeadingDeg + 360f) % 360f

        // Correct for True North using GeomagneticField
        val geoField = GeomagneticField(
            currentLatitude.toFloat(),
            currentLongitude.toFloat(),
            0f,
            System.currentTimeMillis()
        )
        heading = (heading + geoField.declination + 360f) % 360f

        // Low-pass exponential smoothing
        smoothedHeading = smoothAngle(smoothedHeading, heading, 0.25f)

        emitCurrentState(smoothedHeading, accuracy)
    }

    private fun emitCurrentState(heading: Float, accuracy: Int) {
        val relativeAngle = (qiblaBearing - heading + 360f) % 360f
        _qiblaInfoFlow.value = QiblaInfo(
            qiblaBearing = qiblaBearing,
            deviceHeading = heading,
            qiblaRelativeAngle = relativeAngle,
            distanceToKabahKm = distanceToKabahKm,
            sensorAccuracy = accuracy
        )
    }

    private fun smoothAngle(current: Float, target: Float, factor: Float): Float {
        var diff = target - current
        while (diff < -180f) diff += 360f
        while (diff > 180f) diff -= 360f
        return (current + diff * factor + 360f) % 360f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        _qiblaInfoFlow.value = _qiblaInfoFlow.value.copy(sensorAccuracy = accuracy)
    }
}


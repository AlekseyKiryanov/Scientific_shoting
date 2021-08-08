package ru.kyrianov_aleksey.scientific_shoting

import android.R.attr.*
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


class MainActivity : AppCompatActivity() {

    var degrees: Int = 30
    var minutes: Int = 0
    var v: Double = 500.0
    var vmax: Double = 20.0
    var vmin: Double = 1.0
    var dv: Double = 10.0
    var ddv: Double = 0.1
    var l: Double = 1.0
    var dl: Double = 0.5
    var h: Double = 0.0
    var dh: Double = 0.2

    //val g: Double = 9.806_65
    val g: Double = 10.0
    val margin: Int = 50
    //val safety_zone: Int = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)
        paintTarget()
        printValues()


        var gun: ImageView = findViewById(R.id.imageRotate);
        val animation: Animation = RotateAnimation(
            0.toFloat(),
            (0 - degrees).toFloat(), 50.0f, 30.0f
        )
        animation.duration = 300
        animation.fillAfter = true
        gun.startAnimation(animation)


        val button13: ImageButton = findViewById(R.id.button13)
        button13.setOnClickListener {
            breakWin()
            if (minutes < 59) {
                minutes++
            } else {
                if (degrees < 88) {
                    minutes = 0;degrees++
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Опасный угол наклона!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
            printValues()
        }
        val button9: ImageButton = findViewById(R.id.button9)
        button9.setOnClickListener {
            breakWin()
            if (minutes > 0) {
                minutes--
            } else {
                if (degrees > 1) {
                    minutes = 59;degrees--
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Минимальный угол наклона!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
            printValues()
        }
        val button15: ImageButton = findViewById(R.id.button15)
        button15.setOnClickListener {

            if (degrees < 88) {
                val animation: Animation = RotateAnimation(
                    -degrees.toFloat(),
                    (1 - degrees).toFloat(), 50.0f, 30.0f
                )
                animation.duration = 300
                animation.fillAfter = true
                gun.startAnimation(animation)

                breakWin()
                degrees++
                printValues()
            } else {
                breakWin()
                val toast =
                    Toast.makeText(applicationContext, "Опасный угол наклона!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        val button8: ImageButton = findViewById(R.id.button8)
        button8.setOnClickListener {

            if (degrees > 1) {
                val animation: Animation = RotateAnimation(
                    -degrees.toFloat(),
                    (0 - 1 - degrees).toFloat(), 50.0f, 30.0f
                )
                animation.duration = 300
                animation.fillAfter = true
                gun.startAnimation(animation)

                breakWin()
                degrees--
                printValues()
            } else {
                breakWin()
                val toast = Toast.makeText(
                    applicationContext,
                    "Минимальный угол наклона!",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
        val button12: ImageButton = findViewById(R.id.button12)
        button12.setOnClickListener {
            breakWin()
            if (v + ddv <= vmax) {
                v += ddv
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Достигнута максимальная скорость",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            printValues()
        }
        val button7: ImageButton = findViewById(R.id.button7)
        button7.setOnClickListener {
            breakWin()
            if (v - ddv >= vmin) {
                v -= ddv
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Достигнута минимальная скорость",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            printValues()
        }
        val button5: ImageButton = findViewById(R.id.button5)
        button5.setOnClickListener {
            breakWin()
            shot()
        }
        val button14: ImageButton = findViewById(R.id.button14)
        button14.setOnClickListener {
            breakWin()
            paintTarget()
        }
        val button11: ImageButton = findViewById(R.id.button11)
        button11.setOnClickListener {

            breakWin()
            if (v + dv <= vmax) {
                v += dv
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Достигнута максимальная скорость",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            printValues()

        }
        val button6: ImageButton = findViewById(R.id.button6)
        button6.setOnClickListener {
            breakWin()
            if (v - dv >= vmin) {
                v -= dv
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Достигнута минимальная скорость",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            printValues()
        }
    }

    fun printValues() {
        val text_alpha: TextView = findViewById(R.id.textViewA)
        val text_v: TextView = findViewById(R.id.textViewV)
        val text_h: TextView = findViewById(R.id.textViewH)
        val text_l: TextView = findViewById(R.id.textViewL)
        text_alpha.text = "α = $degrees°$minutes′"
        if (l > 5000.0) {
            text_v.text = "υ = ${v.format(0)} м/с"
            text_h.text = "h = ${h.format(0)} + ${dh.format(0)} м"
            text_l.text = "l = ${l.format(0)} + ${dl.format(0)} м"
        } else {
            text_v.text = "υ = ${v.format(2)} м/с"
            text_h.text = "h = ${h.format(2)} + ${dh.format(2)} м"
            text_l.text = "l = ${l.format(2)} + ${dl.format(2)} м"
        }
    }

    fun printShotValues(Smax: Double, Hmax: Double) {
        val text_h: TextView = findViewById(R.id.textViewHmax)
        val text_l: TextView = findViewById(R.id.textViewSmax)
        text_h.text = "Hmax = ${Hmax.format(2)} м"
        text_l.text = "Smax = ${Smax.format(2)} м"
    }


    fun paintTarget() {

        var x: Double = 0.0
        x = Math.random()
        if (x <= 0.6) {
            x = Math.random()
            l = x * 10
            x = Math.random()
            dl = x * 2
            x = Math.random()
            dh = x
            h = 0.0
            vmax = 20.0
            vmin = 1.0
            v = 5.0
            dv = 1.0
            ddv = 0.1
        } else {
            x = Math.random()
            l = 5000.0 + 15000.0 * x
            x = Math.random()
            dl = 30.0 + 60.0 * x
            dh = 60.0
            h = 0.0
            vmax = 1000.0
            vmin = 200.0
            v = 300.0
            dv = 10.0
            ddv = 1.0
        }
        printValues()


        val image: ImageView = findViewById(R.id.imageView)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width: Int = size.x
        var height: Int = size.y
        Log.d("Screen", "w=$width h=$height")


        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.eraseColor(Color.rgb(224, 247, 250));
        image.setImageBitmap(bitmap);

        width = width - margin - margin
        var hh: Double = (height - margin).toDouble()
        var step: Double = 0.0
        var steps: Int = 0


        if ((h + dh) / (l + dl) < hh / width) {
            step = (l + dl) / width
            steps = (((l + dl) / step).toInt() + 1)
        } else {
            step = (h + dh) / hh
            steps = (((h + dh) / step).toInt() + 1)
        }
        steps =
            max((((l + dl) / step).toInt() + 1), (((h + dh) / step).toInt() + 1))

        for (i in (l / step).toInt()..((l + dl) / step).toInt()) {

            for (j in (height - 1 - (h / step).toInt() - (dh / step).toInt())..(height - 1 - (h / step).toInt())) {
                bitmap.setPixel(margin + i, j, Color.GRAY);

            }
        }
    }

    fun shot() {


        val image: ImageView = findViewById(R.id.imageView)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width: Int = size.x
        var height: Int = size.y
        Log.d("Screen", "w=$width h=$height")


        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.eraseColor(Color.rgb(224, 247, 250));
        image.setImageBitmap(bitmap);


        //bitmap.setPixel(50, 50, Color.RED);


        var alpha: Double = minutes * PI / 60 / 180 + degrees * PI / 180
        var Hmax: Double = v * v * sin(alpha) * sin(alpha) / 2 / g
        var Smax: Double = v * v * sin(2 * alpha) / g
        printShotValues(Smax, Hmax)
        var step: Double = 0.0
        var steps: Int = 0
        width = width - margin - margin
        var hh: Double = (height - margin).toDouble()


        var x = (Hmax / Smax)
        var y: Double = (hh / width)
        Log.d("chart2", "$x $y")

        if (max(Hmax, h + dh) / max(Smax, l + dl) < hh / width) {
            step = max(Smax, l + dl) / width
            steps = ((max(Smax, l + dl) / step).toInt() + 1)
        } else {
            step = max(Hmax, h + dh) / hh
            steps = ((max(Hmax, h + dh) / step).toInt() + 1)
        }
        steps =
            max(((max(Smax, l + dl) / step).toInt() + 1), ((max(Hmax, h + dh) / step).toInt() + 1))

        for (i in (l / step).toInt()..((l + dl) / step).toInt()) {

            for (j in (height - 1 - (h / step).toInt() - (dh / step).toInt())..(height - 1 - (h / step).toInt())) {
                bitmap.setPixel(margin + i, j, Color.GRAY);

            }
        }


        var last: Int = 0
        var there: Int = 0
        //((Smax / step).toInt() + 1)


        var win: Boolean = false
        var crit1: Boolean = false
        var crit2: Boolean = false
        var fact: Double = 0.0

        if (((l * sin(alpha) / cos(alpha) - g * l * l / v / v / cos(alpha) / cos(alpha) / 2) <= h + dh) && ((l * sin(
                alpha
            ) / cos(alpha) - g * l * l / v / v / cos(alpha) / cos(alpha) / 2) >= h)
        ) {
            crit1 = true
            win = true
            fact = l
        }

        if ((((l + dl) * sin(alpha) / cos(alpha) - g * (l + dl) * (l + dl) / v / v / cos(alpha) / cos(
                alpha
            ) / 2) <= h + dh) && (((l + dl) * sin(alpha) / cos(alpha) - g * (l + dl) * (l + dl) / v / v / cos(
                alpha
            ) / cos(alpha) / 2) >= h)
        ) {
            crit2 = true
            win = true
            fact = l + dl
        }




        for (i in 0..steps) {


            there =
                (((i * step) * sin(alpha) / cos(alpha) - g * (i * step) * (i * step) / v / v / cos(
                    alpha
                ) / cos(alpha) / 2) / step).toInt()
            if (there < 0) {
                there = 0
            }


            if (crit1 && (i * step) >= l) {
                break
            }
            if (((i * step < (l + dl)) && ((i * step) > l) && ((there * step) > h)) && ((there * step) < (h + dh))) {
                win = true
                fact = i * step
                break
            }
            if (there > last) {
                for (j in (last + 1)..there) {
                    var x = (margin + i + 1)
                    var y = (height - 1 - j)

                    bitmap.setPixel((margin + i), (height - 1 - j), Color.RED);
                    bitmap.setPixel((margin + i + 1), (height - 1 - j), Color.RED);
                    bitmap.setPixel((margin + i + 2), (height - 1 - j), Color.RED);


                }
            }
            if (crit2 && (i * step) >= (l + dl)) {
                break
            }
            if (there < last) {
                for (j in there..(last - 1)) {
                    var x = (margin + i + 1)
                    var y = (height - 1 - j)

                    bitmap.setPixel((margin + i), (height - 1 - j), Color.RED);
                    bitmap.setPixel((margin + i + 1), (height - 1 - j), Color.RED);
                    bitmap.setPixel((margin + i + 2), (height - 1 - j), Color.RED);

                }
            }
            if (there == last) {
                var x = (margin + i + 1)
                var y = (height - 1 - there)

                bitmap.setPixel((margin + i), (height - 1 - there), Color.RED);
                bitmap.setPixel((margin + i + 1), (height - 1 - there), Color.RED);
                bitmap.setPixel((margin + i + 2), (height - 1 - there), Color.RED);



            }
            last = there
        }

        val win1: TextView = findViewById(R.id.textView)
        val win2: TextView = findViewById(R.id.textView2)
        if (win) {
            win1.text = "Попадание!"
            if (fact <= (l + dl / 2)) {
                win2.text = "Точность ${((fact - l) / dl * 200).format(0)} %"
            } else {
                win2.text = "Точность ${((l + dl - fact) / dl * 200).format(0)} %"
            }
            win1.visibility = View.VISIBLE
            win2.visibility = View.VISIBLE
        } else {
            win1.text = "Промах..."
            if (Smax > l + dl) {
                win2.text = "Перелет ${(Smax - l - dl).format(2)} м"
                win2.visibility = View.VISIBLE
            }
            if (Smax < l) {
                win2.text = "Недолет ${(l - Smax).format(2)} м"
                win2.visibility = View.VISIBLE
            }
            win1.visibility = View.VISIBLE

        }


    }


    fun breakWin() {
        val win1: TextView = findViewById(R.id.textView)
        val win2: TextView = findViewById(R.id.textView2)

        win1.visibility = View.INVISIBLE
        win2.visibility = View.INVISIBLE
    }
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
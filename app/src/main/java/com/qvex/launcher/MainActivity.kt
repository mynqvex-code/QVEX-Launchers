import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val bg = Color.rgb(9, 11, 15)
    private val surface = Color.rgb(23, 27, 34)
    private val blue = Color.rgb(65, 145, 255)
    private val muted = Color.rgb(158, 169, 184)
    private var profile = "STABLE FPS"
    private lateinit var deviceText: TextView
    private lateinit var thermalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profile = getPreferences(MODE_PRIVATE).getString("profile", "STABLE FPS") ?: "STABLE FPS"
        render()
        refreshDeviceInfo()
    }

    private fun render() {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(26), dp(20), dp(28))
        }
        scroll.addView(page)

        page.addView(textView("QVEX", 32, Color.WHITE, true))
        page.addView(textView("PERFORMANCE LAUNCHER  •  ANDROID", 10, muted))
        page.addView(gap(22))

        val hero = panel()
        hero.addView(textView("CURRENT PROFILE", 11, muted))
        hero.addView(textView(profile, 25, Color.WHITE, true))
        hero.addView(textView("Ưu tiên FPS cao, ổn định và kiểm soát nhiệt độ.", 13, muted))
        page.addView(hero)
        page.addView(gap(12))

        val profiles = listOf(
            "AUTO" to "Tự chọn cấu hình phù hợp thiết bị",
            "STABLE FPS" to "Ưu tiên FPS đều, giảm khựng hình",
            "HIGH FPS" to "Ưu tiên tốc độ khung hình",
            "MAX POWER" to "Hiệu năng cao trong giới hạn an toàn"
        )
        profiles.forEach { (name, description) ->
            val button = Button(this).apply {
                text = "$name  |  $description"
                isAllCaps = false
                textSize = 12f
                setTextColor(Color.WHITE)
                setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    if (profile == name) blue else surface
                ))
                setOnClickListener {
                    profile = name
                    getPreferences(MODE_PRIVATE).edit().putString("profile", profile).apply()
                    render()
                    refreshDeviceInfo()
                }
            }
            page.addView(button, LinearLayout.LayoutParams(-1, dp(52)).apply {
                bottomMargin = dp(7)
            })
        }

        page.addView(gap(14))
        val monitor = panel()
        monitor.addView(textView("DEVICE MONITOR", 11, muted))
        deviceText = textView("Đang đọc thông tin thiết bị…", 14, Color.WHITE)
        thermalText = textView("Trạng thái nhiệt: đang kiểm tra", 13, muted)
        monitor.addView(deviceText)
        monitor.addView(thermalText)
        val refresh = Button(this).apply {
            text = "LÀM MỚI THÔNG SỐ"
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(surface))
            setOnClickListener { refreshDeviceInfo() }
        }
        monitor.addView(refresh)
        page.addView(monitor)

        page.addView(gap(14))
        val roadmap = panel()
        roadmap.addView(textView("QVEX LAB  •  VERSION 0.1.0", 11, blue, true))
        roadmap.addView(textView(
            "Đã có: giao diện QVEX, lưu chế độ, đọc thông tin Android/RAM và trạng thái nhiệt nếu thiết bị hỗ trợ.",
            13, Color.WHITE
        ))
        roadmap.addView(textView(
            "Chưa có: Minecraft Java engine, đăng nhập Microsoft, tải phiên bản, chạy game và đo FPS trong game. Các chế độ hiện lưu lựa chọn, chưa trực tiếp tăng FPS.",
            13, muted
        ))
        page.addView(roadmap)
        setContentView(scroll)
    }

    private fun refreshDeviceInfo() {
        val memory = ActivityManager.MemoryInfo()
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memory)
        val total = memory.totalMem / (1024 * 1024)
        val available = memory.availMem / (1024 * 1024)
        deviceText.text = "Thiết bị: ${Build.MANUFACTURER} ${Build.MODEL}\\nAndroid ${Build.VERSION.RELEASE} • API ${Build.VERSION.SDK_INT}\\nRAM hệ thống: ${total} MB • Khả dụng: ${available} MB\\nProfile: $profile"

        val power = getSystemService(Context.POWER_SERVICE) as PowerManager
        val thermal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when (power.currentThermalStatus) {
                PowerManager.THERMAL_STATUS_NONE -> "Bình thường"
                PowerManager.THERMAL_STATUS_LIGHT -> "Hơi ấm"
                PowerManager.THERMAL_STATUS_MODERATE -> "Ấm"
                PowerManager.THERMAL_STATUS_SEVERE -> "Nóng – nên giảm tải"
                PowerManager.THERMAL_STATUS_CRITICAL -> "Rất nóng – dừng tác vụ nặng"
                PowerManager.THERMAL_STATUS_EMERGENCY -> "Khẩn cấp"
                PowerManager.THERMAL_STATUS_SHUTDOWN -> "Nguy cơ tắt máy"
                else -> "Không xác định"
            }
        } else "Android chưa hỗ trợ đọc trạng thái nhiệt"
        thermalText.text = "Trạng thái nhiệt Android: $thermal"
    }

    private fun panel() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(16), dp(16), dp(16))
        setBackgroundColor(surface)
    }

    private fun textView(value: String, size: Int, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, dp(4), 0, dp(7))
        }

    private fun gap(height: Int) = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(1, dp(height))
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}

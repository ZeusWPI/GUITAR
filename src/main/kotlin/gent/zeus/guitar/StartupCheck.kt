package gent.zeus.guitar

fun interface StartupCheck {
    fun checkOnStartup(): StartupCheckResult
}

data class StartupCheckResult(
    val checkPassed: Boolean,
    val message: String?,
)

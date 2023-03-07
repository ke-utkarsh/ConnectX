package ymsli.com.ea1h.model

class AppVersionResponse (
    val updateAvailable: Boolean,
    val forceUpdate: Boolean,
    val appUpdateURL: String?,
    val message: String?
)
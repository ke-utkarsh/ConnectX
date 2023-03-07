package ymsli.com.ea1h.model

data class LoginFailureResponse(val error: Error)

data class Error(val code: Int, val message: String)
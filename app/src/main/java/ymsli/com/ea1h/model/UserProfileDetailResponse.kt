package ymsli.com.ea1h.model

data class UserProfileDetailResponse(
    val email: String?,
    val password: String?,
    val fullName: String?,
    val socialMediaId: String?,
    val phoneNumber: String?,
    val isActive: String?,
    val createdOn: String?,
    val updatedOn: String?,
    val profilePic: String?,
    val isEmailVerified: String?,
    val source: String?,
    val userId: String?
)
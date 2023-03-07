package ymsli.com.ea1h.database.entity

data class LoginResponse (

    val user: UserEntity,

    val bikes: List<BikeEntity>,

    val tripHistory : List<TripEntity>
)


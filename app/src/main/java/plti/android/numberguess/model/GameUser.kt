package plti.android.numberguess.model

data class GameUser(
    var firstName:String,
    var lastName:String,
    var userName:String,
    var registrationNumber:Int,
    var gender:Gender = Gender.X,
    var birthday:String="",
    var userRank:Double=0.0)
{
    enum class Gender { M, F, X }

    val fullName:String
        get() =  firstName + " " + lastName
    val initials:String
        get() = firstName.toUpperCase() + lastName.toUpperCase()
}

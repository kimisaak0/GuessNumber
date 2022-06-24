package plti.android.numberguess.random

interface RandomNumberGenerator {
    fun rnd(minInt: Int, maxInt: Int): Int
}
package plti.android.numberguess.random.impl

import plti.android.numberguess.random.RandomNumberGenerator

class StdRandom : RandomNumberGenerator {
    override fun rnd(minInt: Int, maxInt: Int): Int {
        val span = maxInt - minInt + 1
        return minInt + Math.floor(Math.random()*span).toInt()
    }
}


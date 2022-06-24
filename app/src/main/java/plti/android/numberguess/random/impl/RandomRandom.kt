package plti.android.numberguess.random.impl

import plti.android.numberguess.random.RandomNumberGenerator
import java.util.*

class RandomRandom : RandomNumberGenerator {

    override fun rnd(minInt: Int, maxInt: Int): Int {
        val rnd: Random = Random()
        val span = maxInt - minInt + 1
        return minInt + rnd.nextInt(span)
    }
}
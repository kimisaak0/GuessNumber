package plti.android.numberguess.statistics

import java.io.Serializable
import java.text.DecimalFormat

object Statistics {
    data class GameSessionRecord(val numberToGuess:Int, val tries:Int) : Serializable
    val data: ArrayList<GameSessionRecord> = ArrayList()

    fun getStatistics(): List<String> {
        val twoDigits = DecimalFormat().apply {
            maximumFractionDigits = 2
        }
        val triesDistrib = neededTriesDistrib().toSortedMap().toString()
        val triesByNumber = triesByNumberToGuess().toSortedMap().mapValues { me -> twoDigits.format(me.value) }.toString()

        return listOf(
            "총 게임수: ${numberOfSessions()}",
            "평균 시도 횟수: ${averageTriesNeeded()}",
            "시도횟수 표준편차 : ${triesNeededStdDev()}",
            "시도 횟수 히스토그램: ${triesDistrib}",
            "숫자에 따른 시도횟수: ${triesByNumber}"
        )
    }

    fun register(numberToGuess:Int, tries:Int) {
        data.add(GameSessionRecord(numberToGuess,tries))
    }

    private fun numberOfSessions() : Int = data.size

    private fun averageTriesNeeded() : Double =
        if(data.size > 0)
            data.map { rec -> rec.tries }.sum().toDouble() / data.size
        else
            0.0

    private fun triesNeededStdDev() : Double {
        if(data.size < 2 )
            return 0.0

        val avg = averageTriesNeeded()

        return Math.sqrt(
            data.map { rec -> Math.pow(rec.tries - avg, 2.0) }.sum() / (data.size - 1)
        )
    }

    private fun neededTriesDistrib() : Map<Int, Int> =
        data.groupBy( { rec -> rec.tries } ).mapValues { me -> me.value.size }

    private fun triesByNumberToGuess() : Map<Int, Double> =
        data.groupBy({rec -> rec.numberToGuess}).mapValues { me -> me.value.map {it.tries}.sum().toDouble() / me.value.size }
}
package mobi.pooh3.frpstudy.metronome

class Formatters {
    fun formatBpmInfo(msec: Long)  = "$msec msec\n${(60000/msec).toFloat()} BPM"
}
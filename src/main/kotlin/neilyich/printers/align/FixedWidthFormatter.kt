package neilyich.printers.align

import neilyich.printers.Formatter

interface FixedWidthFormatter<T> : Formatter<T> {
    val width: Int
}
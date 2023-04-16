package neilyich.printers.align

import neilyich.printers.Formatter

interface FixedHeightFormatter<T> : Formatter<T> {
    val height: Int
}
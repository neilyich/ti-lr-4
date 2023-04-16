package neilyich.printers

interface Formatter<T> {
    fun format(input: T): List<String>
}

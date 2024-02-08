package ru.aidar.lingolearn.translator

import java.util.Locale

class LingoLearnLanguage(
    private val code: String
) : Comparable<LingoLearnLanguage> {

    fun code() = this.code

    private val displayName: String
        get() = Locale(code).displayName

    override fun compareTo(other: LingoLearnLanguage): Int {
        return displayName.compareTo(other.displayName)
    }

    override fun toString(): String {
        return "$code - $displayName"
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if(other === this) {
            return true
        }

        if(other !is LingoLearnLanguage) {
            return false
        }

        val otherLang = other as LingoLearnLanguage?
        return otherLang!!.code == code
    }

}
package fr.qg.items.common.utils

fun <E> List<List<E>>.linearise() : MutableList<E> =
    this.fold(mutableListOf()) { a, b -> a.addAll(b); a }
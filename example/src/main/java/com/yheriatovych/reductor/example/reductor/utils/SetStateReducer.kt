package com.yheriatovych.reductor.example.reductor.utils

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer

/**
 * Reducer which wrap other [com.yheriatovych.reductor.Reducer] and add one action:
 * "SET_GLOBAL_STATE" to be able to replace state with provided value
 */
class SetStateReducer<T>(private val source: Reducer<T>) : Reducer<T> {

    @Suppress("UNCHECKED_CAST")
    override fun reduce(state: T, action: Action): Pair<T, Commands<T>> {
        return if (action.type == SET_GLOBAL_STATE) {
            Pair.create(action.values as T)
        } else source.reduce(state, action)
    }

    companion object {
        const val SET_GLOBAL_STATE = "SET_GLOBAL_STATE"

        fun <T> setStateAction(value: T): Action {
            return Action.create(SET_GLOBAL_STATE, value)
        }
    }
}

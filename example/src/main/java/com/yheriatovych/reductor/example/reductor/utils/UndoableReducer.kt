package com.yheriatovych.reductor.example.reductor.utils

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.Store

import java.util.LinkedList

/**
 * Reducer which add 'Undo' action to existing reducer.
 */
class UndoableReducer<State>(private val sourceReducer: Reducer<State>) : Reducer<State> {

    private val stack = LinkedList<State>()

    override fun reduce(state: State, action: Action): Pair<State, Commands<State>> {
        if (action.type == "POP") {
            return if (stack.isEmpty()) {
                Pair.create(state)
            } else {
                Pair.create(stack.pop())
            }
        } else if (action.type != Store.INIT_ACTION) {
            stack.push(state)
        }
        return sourceReducer.reduce(state, action)
    }

    companion object {
        fun pop(): Action {
            return Action.create("POP")
        }
    }
}

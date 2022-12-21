package com.juraj.stocksbrowser.ui.common

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

abstract class MviViewModel<STATE : Any, SIDE_EFFECT : Any, INTENT : Any>(initialState: STATE) :
    ContainerHost<STATE, SIDE_EFFECT>,
    IntentHandler<INTENT>,
    ViewModel() {

    override val container = container<STATE, SIDE_EFFECT>(initialState)

    protected fun updateState(newState: (STATE) -> STATE) {
        intent {
            reduce {
                newState(state)
            }
        }
    }

    protected fun postSideEffect(sideEffect: SIDE_EFFECT) {
        intent {
            postSideEffect(sideEffect)
        }
    }
}

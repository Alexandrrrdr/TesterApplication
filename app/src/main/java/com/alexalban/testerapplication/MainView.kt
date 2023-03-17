package com.alexalban.testerapplication

import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SingleStateStrategy::class)
interface MainView: MvpView {
    fun downloaded(path: String, loadingId: Long)
    fun installed()
}
package com.example.applearndriver.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

abstract class BaseViewModel : ViewModel() {

    private val _isLoadingState = MutableStateFlow<Boolean>(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState.asStateFlow()

    @OptIn(ExperimentalContracts::class)
    suspend fun <R> withLoading(
        block: suspend () -> R,
    ): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        return run {
            try {
                activeWithLoading.updateAndGet { it + 1 }
                _isLoadingState.update { true }
                block()
            } finally {
                withContext(NonCancellable) {
                    val newValue = activeWithLoading
                        .updateAndGet { it - 1 }
                        .coerceAtLeast(0)
                    _isLoadingState.update { newValue > 0 }
                }
            }
        }
    }

    private val activeWithLoading = AtomicInteger(0)

}
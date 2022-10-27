package com.monksoft.calculator

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class CalculatorUtilsMockTest{

    @Mock
    lateinit var operations: Operations

    @Mock
    lateinit var listener: OnResolveListener

    @Mock
    lateinit var calculatorUtils: CalculatorUtils

    @Before
    fun setup(){
        calculatorUtils = CalculatorUtils(operations, listener)
    }

    @Test
    fun calc_callCheckOrResolve_noReturn(){
        val operation = "5x2.5"
        val isFromResolve = true

        calculatorUtils.checkOrResolve(operation, isFromResolve)
        verify(operations).tryResolve(operation, isFromResolve, listener)
    }

    @Test
    fun calc_callAddOperator_validSub_noReturn(){
        val operator = "-"
        val operation = "4+"
        var isCorrect = false
        calculatorUtils.addOperator(operator, operation){
            isCorrect = true
        }

        assertTrue(isCorrect)
    }

    @Test
    fun calc_callAddOperator_invalidSub_noReturn(){
        val operator = "-"
        val operation = "4+."
        var isCorrect = false
        calculatorUtils.addOperator(operator, operation){
            isCorrect = true
        }
        assertFalse(isCorrect)
    }

    @Test
    fun calc_callAddOPoint_firstPoint_noReturn(){
        val operation = "4x2"
        var isCorrect = false
        calculatorUtils.addPoint(operation){
            isCorrect = true
        }
        assertTrue(isCorrect)
        verifyNoInteractions(operations)
    }

    @Test
    fun calc_callAddOPoint_secondPoint_noReturn(){
        val operation = "4.5x2"
        val operator = "x"
        var isCorrect = false

        `when`(operations.getOperator(operation)).thenReturn("x")
        `when`(operations.divideOperation(operator, operation)).thenReturn(arrayOf("3.5", "2"))

        calculatorUtils.addPoint(operation){
            isCorrect = true
        }
        assertTrue(isCorrect)
        //a continuacion verifica que los dos metodos se esten ejecutando
        verify(operations).getOperator(operation)
        verify(operations).divideOperation(operator, operation)
    }
}
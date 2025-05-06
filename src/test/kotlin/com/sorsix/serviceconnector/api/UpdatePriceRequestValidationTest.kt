package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.UpdatePriceRequest
import org.junit.jupiter.api.Test
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdatePriceRequestValidationTest {

    private val validator = LocalValidatorFactoryBean().apply { afterPropertiesSet() }

    @Test
    fun `should fail when price is null`() {
        val request = UpdatePriceRequest(price = null)
        val violations = validator.validate(request)
        assertFalse(violations.isEmpty())
        assertEquals("Price must not be null", violations.first().message)
    }

    @Test
    fun `should fail when price is negative`() {
        val request = UpdatePriceRequest(price = "-10.00".toBigDecimal())
        val violations = validator.validate(request)
        assertFalse(violations.isEmpty())
        assertEquals("Price must be greater than 0", violations.first().message)
    }

    @Test
    fun `should pass when price is valid`() {
        val request = UpdatePriceRequest(price = "20.00".toBigDecimal())
        val violations = validator.validate(request)
        assertTrue(violations.isEmpty())
    }
}

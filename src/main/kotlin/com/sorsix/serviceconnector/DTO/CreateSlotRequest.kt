package com.sorsix.serviceconnector.DTO

import com.sorsix.serviceconnector.model.DayOfWeek
import jakarta.validation.constraints.NotNull
import java.time.LocalTime
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidSlotTimeValidator::class])
annotation class ValidSlotTime(
    val message: String = "End time must be after start time",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ValidSlotTimeValidator : ConstraintValidator<ValidSlotTime, CreateSlotRequest> {
    override fun isValid(value: CreateSlotRequest, context: ConstraintValidatorContext): Boolean {
        return value.startTime != null &&
                value.endTime != null &&
                value.endTime.isAfter(value.startTime)
    }
}

@ValidSlotTime
data class CreateSlotRequest(
    @field:NotNull(message = "Start time is required")
    val startTime: LocalTime,

    @field:NotNull(message = "End time is required")
    val endTime: LocalTime,
    @field:NotNull(message = "Day of week is required")
    val dayOfWeek: DayOfWeek,


    val slotId: Long? = null // Optional with null default
)
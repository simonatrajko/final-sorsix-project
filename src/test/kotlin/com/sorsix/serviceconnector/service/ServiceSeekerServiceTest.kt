package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.repository.ServiceSeekerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.util.Optional
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ServiceSeekerServiceTest {

    @Mock
    lateinit var serviceSeekerRepository: ServiceSeekerRepository

    @InjectMocks
    lateinit var serviceSeekerService: ServiceSeekerService

    private val seeker = ServiceSeeker(
        username = "seeker1",
        password = "pass",
        email = "seeker@mail.com",
        fullName = "Seeker One",
        profileImage = "",
        location = "Bitola",
        preferredContactMethod = "email",
        notificationPreferences = "ALL"
    ).apply { id = 10L }

    @Test
    fun `findById should return seeker`() {
        whenever(serviceSeekerRepository.findById(10L)).thenReturn(Optional.of(seeker))
        val result = serviceSeekerService.findById(10L)
        assertEquals(seeker, result)
    }

    @Test
    fun `findByUsername should return seeker`() {
        whenever(serviceSeekerRepository.findByUsername("seeker1")).thenReturn(seeker)
        val result = serviceSeekerService.findByUsername("seeker1")
        assertEquals(seeker, result)
    }

    @Test
    fun `getAllSeekers should return list of seekers`() {
        val seekers = listOf(seeker)
        whenever(serviceSeekerRepository.findAll()).thenReturn(seekers)
        val result = serviceSeekerService.getAllSeekers()
        assertEquals(seekers, result)
    }

    @Test
    fun `saveSeeker should save and return seeker`() {
        whenever(serviceSeekerRepository.save(seeker)).thenReturn(seeker)
        val result = serviceSeekerService.saveSeeker(seeker)
        assertEquals(seeker, result)
    }
}

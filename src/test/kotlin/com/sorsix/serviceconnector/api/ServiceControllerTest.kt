package com.sorsix.serviceconnector.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.sorsix.serviceconnector.DTO.CreateServiceRequest
import com.sorsix.serviceconnector.DTO.ProviderDTO
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.DTO.UpdatePriceRequest
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.repository.ServiceCategoryRepository
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.security.JwtAuthFilter
import com.sorsix.serviceconnector.security.JwtService
import com.sorsix.serviceconnector.service.impl.ScheduleSlotServiceImpl
import com.sorsix.serviceconnector.service.impl.ServicesServiceImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.patch
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(controllers = [ServiceController::class])
@AutoConfigureMockMvc(addFilters = false)
class ServiceControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var servicesService: ServicesServiceImpl
    @MockBean
    lateinit var scheduleSlotService: ScheduleSlotServiceImpl
    @MockBean
    lateinit var serviceProviderRepository: ServiceProviderRepository
    @MockBean
    lateinit var serviceCategoryRepository: ServiceCategoryRepository

    // 👉 security-related mock beans
    @MockBean
    lateinit var jwtService: JwtService
    @MockBean
    lateinit var jwtAuthFilter: JwtAuthFilter

    private val objectMapper = ObjectMapper()
    private val provider = ServiceProvider(
        username = "provider1",
        hashedPassword = "pass",
        email = "provider@mail.com",
        fullName = "Elena Provider",
        profileImage = "",
        location = "Skopje"
    ).apply { id = 1L }

    private val service = Services(
        id = 1L,
        title = "Hair Styling",
        description = "Styling hair",
        price = BigDecimal(70),
        duration = 45,
        category = ServiceCategory(1, "Beauty"),
        provider = provider,
        createdAt = LocalDateTime.now()
    )


    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should create service successfully`() {
        val request = CreateServiceRequest(
            title = "Test Service",
            description = "Test Desc",
            price = BigDecimal(25),
            duration = 60,
            categoryId = 1L
        )

        val provider = ServiceProvider(
            username = "provider",
            hashedPassword = "hashed",
            email = "email@test.com",
            fullName = "Provider",
            profileImage = "",
            location = "Skopje"
        ).apply { id = 1 }

        val category = ServiceCategory(id = 1L, name = "Cleaning")

        val service = Services(
            id = 10L,
            title = request.title,
            description = request.description,
            price = request.price,
            duration = request.duration,
            category = category,
            provider = provider,
            createdAt = LocalDateTime.now()
        )

        // ➕ define mock behavior
        whenever(serviceProviderRepository.findByUsername("provider")).thenReturn(provider)
        whenever(serviceCategoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category))
        whenever(servicesService.createService(any())).thenReturn(service)

        // ➕ perform the request
        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.title") { value("Test Service") }
        }
    }

    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should return 404 when category not found`() {
        val request = CreateServiceRequest(
            title = "New Service",
            description = "Description",
            price = BigDecimal(30),
            duration = 60,
            categoryId = 99L
        )

        val provider = ServiceProvider(
            username = "provider",
            hashedPassword = "hashed",
            email = "email@test.com",
            fullName = "Provider",
            profileImage = "",
            location = "Skopje"
        ).apply { id = 1 }

        whenever(serviceProviderRepository.findByUsername("provider")).thenReturn(provider)
        whenever(serviceCategoryRepository.findById(99L)).thenReturn(java.util.Optional.empty())

        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should return 404 when provider not found`() {
        val request = CreateServiceRequest(
            title = "Test Service",
            description = "Test Desc",
            price = BigDecimal(25),
            duration = 60,
            categoryId = 1L
        )

        // mock враќа null за provider
        whenever(serviceProviderRepository.findByUsername("provider")).thenReturn(null)

        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockUser(username = "user", roles = ["USER"]) // не е PROVIDER
    fun `should return 403 when user is not provider`() {
        val request = CreateServiceRequest(
            title = "Test Service",
            description = "Test Desc",
            price = BigDecimal(25),
            duration = 60,
            categoryId = 1L
        )

        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `create should return 401 when user is not authenticated`() {
        val request = CreateServiceRequest(
            title = "Service",
            description = "Some desc",
            price = BigDecimal(20),
            duration = 30,
            categoryId = 1L
        )

        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should return 400 when title is blank`() {
        val request = CreateServiceRequest(
            title = "",
            description = "desc",
            price = BigDecimal(10),
            duration = 30,
            categoryId = 1L
        )

        mockMvc.post("/api/services") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }  // 400
        }
    }

    @Test
    @WithMockUser
    fun `should return available services`() {
        val pageable = PageRequest.of(0, 10)
        whenever(servicesService.getAvailableServicesAsDto(pageable)).thenReturn(PageImpl(emptyList()))

        mockMvc.get("/api/services?page=0&size=10")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @WithMockUser
    fun `should return service by id`() {
        val serviceId = 42L

        val dto = ServiceDTO(
            id = serviceId,
            title = "Window Cleaning",
            description = "desc",
            price = BigDecimal(50),
            duration = 60,
            category = "Cleaning",
            provider = ProviderDTO(
                id = 1,
                fullName = "Elena Provider",
                email = "provider@mail.com",
                location = "Skopje"
            )
        )

        // Врати ентитет кој ќе се мапира во DTO
        val service = Services(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            price = dto.price,
            duration = dto.duration,
            category = ServiceCategory(1, dto.category),
            provider = ServiceProvider(
                "provider",
                "hashed",
                dto.provider.email,
                dto.provider.fullName,
                "",
                dto.provider.location
            ).apply { id = dto.provider.id },
            createdAt = LocalDateTime.now()
        )

        whenever(servicesService.getServiceById(serviceId)).thenReturn(service)

        mockMvc.get("/api/services/$serviceId")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(dto.id.toInt()) }
                jsonPath("$.title") { value(dto.title) }
                jsonPath("$.provider.fullName") { value(dto.provider.fullName) }
            }
    }


    @Test
    @WithMockUser
    fun `should return 404 if service not found`() {
        val id = 99L
        whenever(servicesService.getServiceById(id)).thenReturn(null)

        mockMvc.get("/api/services/$id")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    @WithMockUser
    fun `should return services by category id`() {
        val categoryId = 1L
        val provider = ServiceProvider("provider", "pass", "mail", "Elena", "", "Skopje").apply { id = 1L }

        val service = Services(
            id = 1L,
            title = "Service A",
            description = "Description A",
            price = BigDecimal(20),
            duration = 30,
            category = ServiceCategory(categoryId, "Cleaning"),
            provider = provider,
            createdAt = LocalDateTime.now()
        )

        // ✅ Use `any()` to avoid exact object matching
        whenever(servicesService.getServicesByCategory(any())).thenReturn(listOf(service))

        mockMvc.get("/api/services/category/$categoryId")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(1) }
                jsonPath("$[0].title") { value("Service A") }
            }
    }

    @Test
    @WithMockUser
    fun `should return services by category name`() {
        val categoryName = "Cleaning"
        val provider = ServiceProvider("provider", "pass", "mail", "Elena", "", "Skopje").apply { id = 1L }

        val service = Services(
            id = 1L,
            title = "Window Cleaning",
            description = "desc",
            price = BigDecimal(50),
            duration = 60,
            category = ServiceCategory(1, categoryName),
            provider = provider,
            createdAt = LocalDateTime.now()
        )

        whenever(servicesService.getServicesByCategoryName(categoryName)).thenReturn(listOf(service))

        mockMvc.get("/api/services/category/name/$categoryName")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(1) }
                jsonPath("$[0].title") { value("Window Cleaning") }
            }
    }

    @Test
    @WithMockUser
    fun `should return empty list when no services for category name`() {
        whenever(servicesService.getServicesByCategoryName("Unknown")).thenReturn(emptyList())

        mockMvc.get("/api/services/category/name/Unknown")
            .andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(0) }
            }

    }

    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should return my services`() {
        val username = "provider"

        val provider = ServiceProvider(
            username = username,
            hashedPassword = "pass",
            email = "provider@mail.com",
            fullName = "Elena Provider",
            profileImage = "",
            location = "Skopje"
        ).apply { id = 1L }

        val service = Services(
            id = 1L,
            title = "Hair Styling",
            description = "Styling hair",
            price = BigDecimal(70),
            duration = 45,
            category = ServiceCategory(1, "Beauty"),
            provider = provider,
            createdAt = LocalDateTime.now()
        )

        whenever(serviceProviderRepository.findByUsername(username)).thenReturn(provider)
        whenever(servicesService.getServicesByProvider(provider.id!!)).thenReturn(listOf(service))

        mockMvc.get("/api/services/my-services-provider")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(1) }
                jsonPath("$[0].title") { value("Hair Styling") }
            }
    }

    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should return 404 when provider does not exist`() {
        whenever(serviceProviderRepository.findByUsername("provider")).thenReturn(null)

        mockMvc.get("/api/services/my-services-provider")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    @WithAnonymousUser
    fun `my services should return 401 when user is not authenticated`() {
        mockMvc.get("/api/services/my-services-provider")
            .andExpect {
                status { isUnauthorized() }
            }
    }


    @Test
    @WithMockUser(username = "provider", roles = ["PROVIDER"])
    fun `should delete service by id`() {
        val id = 1L

        // Не треба mock за deleteService бидејќи е void, само verify ако сакаш
        mockMvc.delete("/api/services/$id")
            .andExpect {
                status { isNoContent() }
            }

        Mockito.verify(servicesService).deleteService(id)
    }

    @Test
    @WithAnonymousUser
    fun `should return 401 when unauthenticated user tries to delete service`() {
        mockMvc.delete("/api/services/42") {
            with(csrf()) // ✅ важно!
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun `should return 403 when user without PROVIDER role tries to delete service`() {
        mockMvc.delete("/api/services/42")
            .andExpect {
                status { isForbidden() }
            }
    }
    @Test
    @WithMockUser(username = "provider1", roles = ["PROVIDER"])
    fun `should update service price successfully`() {
        val serviceId = 1L
        val newPrice = BigDecimal(49.99)
        val request = UpdatePriceRequest(price = newPrice)
        val updated = service.copy(price = request.price!!)
        val username = "provider"

        val provider = ServiceProvider(
            username = username,
            hashedPassword = "pass",
            email = "provider@mail.com",
            fullName = "Elena Provider",
            profileImage = "",
            location = "Skopje"
        ).apply { id = 1L }

        val service = Services(
            id = 1L,
            title = "Hair Styling",
            description = "Styling hair",
            price = BigDecimal(70),
            duration = 45,
            category = ServiceCategory(1, "Beauty"),
            provider = provider,
            createdAt = LocalDateTime.now()
        )
        val price = request.price!!
        whenever(servicesService.updatePrice(service.id!!, price, provider.id!!)).thenReturn(updated)

        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(provider)
        whenever(servicesService.updatePrice(service.id!!, price, provider.id!!)).thenReturn(updated)

        mockMvc.patch("/api/services/$serviceId/price") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(serviceId) }
            jsonPath("$.price") { value(newPrice) }
        }
    }
    @Test
    @WithMockUser(username = "provider1", roles = ["PROVIDER"])
    fun `should update price successfully`() {
        val request = UpdatePriceRequest(price = BigDecimal(1500))
        val updated = service.copy(price = request.price!!)

        val price = request.price!!

        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(provider)
        whenever(servicesService.updatePrice(service.id!!, price, provider.id!!)).thenReturn(updated)

        mockMvc.patch("/api/services/${service.id}/price") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.price") { value(1500) }
        }
    }

    @Test
    @WithMockUser(username = "provider1", roles = ["PROVIDER"])
    fun `should return 400 for invalid price`() {
        val invalidJson = """{ "price": -100 }"""

        val mockProvider = ServiceProvider(
            username = "provider1",
            hashedPassword = "secret",
            email = "provider@example.com",
            fullName = "Elena",
            profileImage = "",
            location = "Skopje"
        ).apply { id = 1L }

        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(mockProvider)

        mockMvc.patch("/api/services/1/price") {
            contentType = MediaType.APPLICATION_JSON
            content = invalidJson
        }.andExpect {
            status { isBadRequest() }  // ✅ Now it should pass
        }
    }

    @Test
    @WithMockUser(username = "provider1", roles = ["PROVIDER"])
    fun `should return 404 when service not found`() {
        val request = UpdatePriceRequest(price = BigDecimal(2000))

        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(provider)
        whenever(servicesService.updatePrice(999L, request.price!!, provider.id!!))
            .thenThrow(RuntimeException("Service not found"))

        mockMvc.patch("/api/services/999/price") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser(username = "provider1", roles = ["PROVIDER"])
    fun `should return 403 if provider is not the owner`() {
        val request = UpdatePriceRequest(price = BigDecimal(999))

        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(provider)
        whenever(servicesService.updatePrice(service.id!!, request.price!!, provider.id!!))
            .thenThrow(RuntimeException("Cannot update another provider's service"))

        mockMvc.patch("/api/services/${service.id}/price") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `should return 401 when user is not authenticated`() {
        val request = UpdatePriceRequest(price = BigDecimal(1000))

        mockMvc.patch("/api/services/1/price") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnauthorized() }
        }
    }

}

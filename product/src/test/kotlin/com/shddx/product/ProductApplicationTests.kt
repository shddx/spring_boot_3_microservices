package com.shddx.product

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
internal class ProductApplicationTests(@Autowired val mockMvc: MockMvc, @Autowired val productRepo: ProductRepo) {
    val mapper: ObjectMapper = ObjectMapper()

    companion object {
        @Container
        val mongoDBContainer = MongoDBContainer("mongo:6.0.4")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
        }
    }

    @Test
    fun `should create product`() {
        val newProduct = productRequest()
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(newProduct)))
            .andExpect(status().isCreated)
        assertThat(productRepo.findAll()).hasSize(1)
    }

    fun productRequest(): ProductRequest {
        return ProductRequest(
            name = "MacBook M2",
            description = "Laptop",
            price = BigDecimal.valueOf(2000)
        )
    }
}

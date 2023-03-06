package com.shddx.order

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
internal class InventoryClientConfig(
    @Value("\${integration.inventory.uri}") val inventoryUri: String
) {
    @Bean
    fun inventoryClient(builder: WebClient.Builder): InventoryClient {
        val webClient = builder.baseUrl(inventoryUri)
            .build()
        return HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build()
            .createClient(InventoryClient::class.java)
    }

    @Bean
    @LoadBalanced
    fun webClient() = WebClient.builder()
}
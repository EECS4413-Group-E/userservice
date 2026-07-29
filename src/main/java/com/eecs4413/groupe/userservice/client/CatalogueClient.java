package com.eecs4413.groupe.userservice.client;

import com.eecs4413.groupe.userservice.client.model.request.ProductExistenceRequest;
import com.eecs4413.groupe.userservice.client.model.response.ListingExistenceResponse;
import com.eecs4413.groupe.userservice.client.model.response.ProductExistenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "catalogue-client", url = "${client.catalogue.url}", fallbackFactory = CatalogueClientFallbackFactory.class)
public interface CatalogueClient {

    @GetMapping("/listing/existence/{id}")
    ListingExistenceResponse checkListingExistenceById(@PathVariable UUID id);

    @PostMapping("/listing/existence/batch")
    List<ListingExistenceResponse> checkListingExistenceBatch(@RequestBody List<UUID> ids);

    @PostMapping("/product/existence")
    ProductExistenceResponse checkProductSizeExistence(@RequestBody ProductExistenceRequest request);

    @PostMapping("/product/existence/batch")
    List<ProductExistenceResponse> checkProductSizeExistenceBatch(@RequestBody List<ProductExistenceRequest> request);
}

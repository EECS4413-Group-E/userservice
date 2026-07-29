package com.eecs4413.groupe.userservice.client;

import com.eecs4413.groupe.userservice.client.model.request.ProductExistenceRequest;
import com.eecs4413.groupe.userservice.client.model.response.ListingExistenceResponse;
import com.eecs4413.groupe.userservice.client.model.response.ProductExistenceResponse;
import com.eecs4413.groupe.userservice.exception.CatalogueServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CatalogueClientFallbackFactory implements FallbackFactory<CatalogueClient> {

    @Override
    public CatalogueClient create(Throwable cause) {
        return new CatalogueClient() {

            @Override
            public ListingExistenceResponse checkListingExistenceById(UUID id) {
                if (cause instanceof FeignException feignException) {
                    throw new CatalogueServiceException(
                            feignException.status(),
                            extractErrorMessage(feignException)
                    );
                }

                throw new CatalogueServiceException();
            }

            @Override
            public List<ListingExistenceResponse> checkListingExistenceBatch(List<UUID> ids) {
                if (cause instanceof FeignException feignException) {
                    throw new CatalogueServiceException(
                            feignException.status(),
                            extractErrorMessage(feignException)
                    );
                }

                throw new CatalogueServiceException();
            }

            @Override
            public ProductExistenceResponse checkProductSizeExistence(ProductExistenceRequest request) {
                if (cause instanceof FeignException feignException) {
                    throw new CatalogueServiceException(
                            feignException.status(),
                            extractErrorMessage(feignException)
                    );
                }

                throw new CatalogueServiceException();
            }

            @Override
            public List<ProductExistenceResponse> checkProductSizeExistenceBatch(List<ProductExistenceRequest> request) {
                if (cause instanceof FeignException feignException) {
                    throw new CatalogueServiceException(
                            feignException.status(),
                            extractErrorMessage(feignException)
                    );
                }

                throw new CatalogueServiceException();
            }
        };
    }

    private String extractErrorMessage(FeignException ex) {
        try {
            String body = ex.contentUTF8();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(body);
            return node.has("message") ? node.get("message").asText() : ex.getMessage();
        } catch (Exception e) {
            return ex.getMessage();
        }
    }
}

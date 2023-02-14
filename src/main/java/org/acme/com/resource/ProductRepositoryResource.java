package org.acme.com.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.acme.com.model.Product;
import org.acme.com.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import io.quarkus.panache.common.Sort;

@Path("product")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ProductRepositoryResource {

    @Inject
    ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepositoryResource.class.getName());

    @GET
    public List<Product> get() {
        return productRepository.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Product getSingle(Long id) {
      Product entity = productRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Product product) {
        if (product.getName() == "") {
            throw new WebApplicationException("Product was invalidly set on request.", 422);
        }
        productRepository.persist(product);
        return Response.ok(product).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Product update(Long id, Product product) {
        if (product.getName() == null) {
            throw new WebApplicationException("Product was not set on request.", 422);
        }

        Product entity = productRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
        }

        entity.setName(product.getName());  
        entity.setDescription(product.getDescription());
        entity.setAvailableQuantity(product.getAvailableQuantity());
        
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
      Product entity = productRepository.findById(id);
      if (entity == null) {
          throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
      }

      productRepository.delete(entity);
      return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
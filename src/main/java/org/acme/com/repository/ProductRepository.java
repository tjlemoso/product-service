package org.acme.com.repository;

import javax.enterprise.context.ApplicationScoped;
import org.acme.com.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
  
}

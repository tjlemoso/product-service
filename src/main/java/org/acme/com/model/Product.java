package org.acme.com.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {

  @Id
  @Column(name = "productId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Column(name = "createDate")
  private LocalDate createDate;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "availableQuantity")
  private Long availableQuantity;
  
  @Column(name = "warehouseId")
  private Long warehouseId;

  @Column(name = "supplierId")
  private Long supplierId;
}

CREATE TABLE `product` (
  `productId` int NOT NULL AUTO_INCREMENT,
  `createDate` date DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `availableQuantity` int DEFAULT NULL,
  `supplierId` bigint DEFAULT NULL,
  `warehouseId` bigint DEFAULT NULL,
  PRIMARY KEY (`productId`)
) ;
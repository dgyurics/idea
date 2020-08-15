package idea.service;

import idea.model.dto.ProductRequestModel;
import idea.model.entity.Product;
import java.util.Collection;

public interface ProductService {
  Collection<Product> getProducts();
  void createProduct(ProductRequestModel product);
  void updateProduct(ProductRequestModel product, Long id);
  void removeProduct(Long id);
}

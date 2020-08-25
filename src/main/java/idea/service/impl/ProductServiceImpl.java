package idea.service.impl;

import idea.model.dto.ProductRequestModel;
import idea.model.entity.Product;
import idea.repository.ProductRepository;
import idea.service.ProductService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository repository;
  private final ModelMapper mapper;

  @Cacheable(value="productCache")
  @Override
  public Collection<Product> getProducts() {
    return repository.findAllByOrderByPriorityAscCreateTimestampAsc();
  }

  @CacheEvict(value="productCache", allEntries=true)
  @Override
  public void createProduct(ProductRequestModel productDto) {
    Product product = new Product();
    mapper.map(productDto, product);
    repository.save(product);
  }

  @CacheEvict(value="productCache", allEntries=true)
  @Override
  public void updateProduct(ProductRequestModel productDto, @NonNull Long id) {
    Product product = new Product();
    mapper.map(productDto, product);
    product.setId(id);
    repository.save(product);
  }

  @CacheEvict(value="productCache", allEntries=true)
  @Override
  public void removeProduct(@NonNull Long id) {
    repository.deleteById(id);
  }
}

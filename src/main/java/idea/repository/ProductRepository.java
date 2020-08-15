package idea.repository;

import idea.model.entity.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
  @Override
  List<Product> findAll();

  @Override
  Product save(Product product);

  @Override
  void deleteById(Long id);
}

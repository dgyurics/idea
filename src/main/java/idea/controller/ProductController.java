package idea.controller;

import idea.model.dto.ProductRequestModel;
import idea.model.entity.Product;
import idea.service.ProductService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService service;

  @GetMapping
  public Collection<Product> getProducts() {
    return service.getProducts();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void createProduct(@RequestBody @Validated ProductRequestModel product) {
    service.createProduct(product);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void updateProduction(
      @RequestBody @Validated ProductRequestModel product,
      @NonNull @PathVariable long id) {
    service.updateProduct(product, id);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void removeProduct(@NonNull @PathVariable long id) {
    service.removeProduct(id);
  }
}

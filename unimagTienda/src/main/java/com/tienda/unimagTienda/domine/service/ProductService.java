package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.ProductDto.*;
import java.util.List;

public interface ProductService {
    ProductResponse create(ProductCreateRequest req);
    ProductResponse get(Long id);
    ProductResponse update(Long id, ProductCreateRequest req);
    List<ProductResponse> getAll();
    void delete(Long id);
}

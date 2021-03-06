package com.tthieu.service;

import com.tthieu.model.ProductModel;

import java.util.List;

public interface IProductService {
    List<ProductModel> findAll();

    ProductModel findOne(int id);

    List<ProductModel> findLatest();

    List<ProductModel> findMostView();

    List<ProductModel> findByName(String name);

    List<ProductModel> findByCategoryId(int id);

    int add(ProductModel model);

    void update(ProductModel model);

    void delete(int id);

    int count(String sql, Object... parameters);
}

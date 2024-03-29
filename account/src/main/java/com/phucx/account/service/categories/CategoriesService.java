package com.phucx.account.service.categories;

import com.phucx.account.model.Categories;

public interface CategoriesService {
    boolean updateCategory(Categories category);
    boolean createCategory(Categories category);
}

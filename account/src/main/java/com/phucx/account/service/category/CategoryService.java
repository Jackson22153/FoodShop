package com.phucx.account.service.category;

import com.phucx.account.model.Category;

public interface CategoryService {
    boolean updateCategory(Category category);
    boolean createCategory(Category category);
}

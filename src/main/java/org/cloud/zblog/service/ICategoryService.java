package org.cloud.zblog.service;

import org.cloud.zblog.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by d05660ddw on 2017/3/6.
 */
public interface ICategoryService extends IService<Category> {
    Optional<Category> getCategoryByName(String name);

    long saveOrUpdateCategory(Category category);

    List<Category> getAllByOrder(String column, String orderDir, int pageNum, int pageSize);
}

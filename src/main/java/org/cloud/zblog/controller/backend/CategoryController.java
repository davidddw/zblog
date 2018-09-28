/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 d05660@163.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.cloud.zblog.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.zblog.controller.base.AdminBaseController;
import org.cloud.zblog.controller.exception.ResourceNotFoundException;
import org.cloud.zblog.model.Article;
import org.cloud.zblog.model.Category;
import org.cloud.zblog.model.ResponseMessage;
import org.cloud.zblog.service.IArticleService;
import org.cloud.zblog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by d05660ddw on 2017/3/20.
 */
@RestController("categoryController")
public class CategoryController extends AdminBaseController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IArticleService articleService;

    /**
     * 新增
     */
    @PostMapping("/category")
    public @ResponseBody ResponseMessage addCategory(@RequestBody Category category) {
        long number = categoryService.saveOrUpdateCategory(category);
        return new ResponseMessage(String.valueOf(number));
    }

    /**
     * 更新
     */
    @PutMapping("/category/{id}")
    public @ResponseBody ResponseMessage updateCategory(@PathVariable String id,
            @RequestBody Category category) {
        category.setId(Long.parseLong(id));
        long number = categoryService.saveOrUpdateCategory(category);
        return new ResponseMessage(String.valueOf(number));
    }

    /**
     * 查询
     */
    @GetMapping("/category")
    public @ResponseBody Map<String, Object> listCategory(@RequestParam String draw,
            @RequestParam int startIndex, @RequestParam int pageSize,
            @RequestParam(value = "orderColumn", required = false, defaultValue = "id") String orderColumn,
            @RequestParam(value = "orderDir", required = false, defaultValue = "asc") String orderDir) {
        Map<String, Object> info = new HashMap<>();
        info.put("pageData",
                categoryService.getAllByOrder(orderColumn, orderDir, startIndex, pageSize));
        info.put("total", categoryService.getCount(new Category()));
        info.put("draw", draw);
        return info;
    }

    /**
     * 删除，除了第一个其他允许删除
     */
    @DeleteMapping("/category/{id}")
    public @ResponseBody ResponseMessage removeCategory(@PathVariable String id) {
        Category defaultCategory = categoryService.getById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Page", id));
        if (!id.equals("1")) {
            Category category = categoryService.getById(Long.parseLong(id))
                    .orElseThrow(() -> new ResourceNotFoundException("Page", id));
            List<Article> articleList = articleService.getArticlesByCategory(category);
            for (Article article : articleList) {
                article.setCategory(defaultCategory);
                articleService.save(article);
            }
            new ResponseMessage(String.valueOf(categoryService.deleteById(category.getId())));
        }
        return new ResponseMessage("Cannot Delete", "0");
    }
}

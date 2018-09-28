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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.zblog.controller.base.AdminBaseController;
import org.cloud.zblog.controller.exception.ResourceNotFoundException;
import org.cloud.zblog.model.Article;
import org.cloud.zblog.model.ResponseMessage;
import org.cloud.zblog.model.Tag;
import org.cloud.zblog.service.IArticleService;
import org.cloud.zblog.service.ITagService;
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
 * Created by d05660ddw on 2017/4/1.
 */
@RestController
public class TagController extends AdminBaseController {

    @Autowired
    private ITagService tagService;

    @Autowired
    private IArticleService articleService;

    /**
     * 新增
     */
    @PostMapping("/tag")
    public @ResponseBody ResponseMessage addTag(@RequestBody Tag tag) {
        long number = tagService.saveOrUpdateTag(tag);
        return new ResponseMessage(String.valueOf(number));
    }

    /**
     * 更新
     */
    @PutMapping("/tag/{id}")
    public @ResponseBody ResponseMessage updateTag(@PathVariable String id, @RequestBody Tag tag) {
        tag.setId(Long.parseLong(id));
        long number = tagService.saveOrUpdateTag(tag);
        return new ResponseMessage(String.valueOf(number));
    }

    /**
     * 查询
     */
    @GetMapping("/tag")
    public @ResponseBody Map<String, Object> listTag(@RequestParam String draw,
            @RequestParam int startIndex, @RequestParam int pageSize,
            @RequestParam(value = "orderColumn", required = false, defaultValue = "count") String orderColumn,
            @RequestParam(value = "orderDir", required = false, defaultValue = "desc") String orderDir) {
        Map<String, Object> info = new HashMap<>();
        tagService.setTagCount();
        info.put("pageData", tagService.getAllByOrder(orderColumn, orderDir, startIndex, pageSize));
        info.put("total", tagService.getCount(new Tag()));
        info.put("draw", draw);
        return info;
    }

    /**
     * 删除
     */
    @DeleteMapping("/tag/{id}")
    public @ResponseBody ResponseMessage removeTag(@PathVariable String id) {
        Tag tag = tagService.getById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Tag", id));
        List<Article> articleList = articleService.getArticlesByTag(tag);
        for (Article article : articleList) {
            String finalString = subString(article.getTagStrings(), tag.getName());
            article.setTagStrings(finalString);
            articleService.save(article);
        }
        return new ResponseMessage(String.valueOf(tagService.deleteSafetyById(tag.getId())));
    }

    private String subString(String tagStrings, String name) {
        String[] str = tagStrings.split(",");
        List<String> list = Arrays.asList(str);
        list.remove(list.indexOf(name));
        return String.join(",", list.toArray(new String[1]));
    }
}

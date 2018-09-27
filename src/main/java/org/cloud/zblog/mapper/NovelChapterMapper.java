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
package org.cloud.zblog.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.zblog.model.NovelChapter;
import org.cloud.zblog.utils.BaseMapper;

import java.util.List;

/**
 * Created by d05660ddw on 2017/3/6.
 */
public interface NovelChapterMapper extends BaseMapper<NovelChapter> {
    List<NovelChapter> selectAllOrderBy(@Param("condition") String condition, @Param("column") String orderColumn, @Param("orderDir") String orderDir);

    Long selectCountByNovelId(@Param("id") Long id);

    List<NovelChapter> selectChaptersByNovelId(@Param("id") Long novelId);

    NovelChapter selectDetailsByChaptersAndNovelId(@Param("novelid") Long novelId, @Param("chapterid") Long chapterId);

    List<NovelChapter> selectPrevNovels(@Param("novelid") Long novelId, @Param("chapterid") Long chapterId);

    List<NovelChapter> selectNextNovels(@Param("novelid") Long novelId, @Param("chapterid") Long chapterId);

    Long insertByBatch(List<NovelChapter> novelChapters);

    Long deleteByBatch(List<Long> novelChaptersIds);

    List<NovelChapter> selectAllByNovelIdOrderBy(@Param("id") Long novelId, @Param("column") String orderColumn, @Param("orderDir") String orderDir);

    long selectCountByCondition(@Param("condition") String condition, NovelChapter novelChapter);
}

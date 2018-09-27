package org.cloud.zblog.service;

import org.cloud.zblog.model.Article;
import org.cloud.zblog.model.Comment;
import org.cloud.zblog.dto.CommentInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by d05660ddw on 2017/3/6.
 */
public interface ICommentService extends IService<Comment>, Serializable {
    List<Comment> getAllByArticle(Article article, int pageNum, int pageSize);

    List<Comment> getTopByDateDesc();

    List<Comment> getAll(int pageNum, int pageSize);

    Long addNewComment(CommentInfo commentInfo, Article article);
}

package org.cloud.zblog.service;

import org.cloud.zblog.model.Album;
import org.cloud.zblog.model.Gallery;

import java.util.List;

/**
 * Created by d05660ddw on 2017/4/12.
 */
public interface IGalleryService extends IService<Gallery> {

    long saveOrUpdateGallery(Gallery gallery);

    List<Gallery> getAllByOrder(String orderColumn, String orderDir, int startIndex, int pageSize);

    List<Gallery> getGalleryByAlbum(Album album);

    List<Gallery> getAllGallery(int pageNum, int pageSize);
}

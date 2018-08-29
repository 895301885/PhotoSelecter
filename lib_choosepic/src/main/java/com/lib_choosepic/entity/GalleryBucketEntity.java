package com.lib_choosepic.entity;

import java.util.ArrayList;

/**
 * @author lisg
 * @since 18-07-26 15:32
 */
public class GalleryBucketEntity {
    /**
     * 文件夹名称
     */
    private String name;
    public boolean isSelected;
    /**
     * 该目录下的图片
     */
    private ArrayList<GalleryEntity> galleries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<GalleryEntity> getGalleries() {
        return galleries;
    }

    public void setGalleries(ArrayList<GalleryEntity> galleries) {
        this.galleries = galleries;
    }

    public int getCount() {
        return galleries == null ? 0 : galleries.size();
    }

    public String getLastPath() {
        if (galleries == null || galleries.size() == 0) {
            return "";
        }
        return galleries.get(galleries.size() - 1).getSdcardPath();
    }

    /**
     * 添加图片
     *
     * @param gallery
     */
    public void addGallery(GalleryEntity gallery) {
        if (galleries == null) {
            galleries = new ArrayList<>();
        }
        galleries.add(gallery);
    }
}

package ucai.cn.day_filicenter.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CategoryChildBean implements Serializable {

    /**
     * id : 345
     * parentId : 344
     * name : 热门
     * imageUrl : cat_image/256_1.png
     */

    private int id;
    private int parentId;
    private String name;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryChildBean() {
    }

    public CategoryChildBean(int id, int parentId, String imageUrl, String name) {
        this.id = id;
        this.parentId = parentId;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryChildBean{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

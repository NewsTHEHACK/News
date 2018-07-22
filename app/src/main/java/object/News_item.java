package object;

import android.graphics.Bitmap;

public class News_item {
    private String tag,person,id;
    private Bitmap bitmap;


    public News_item(String tag, String person, Bitmap bitmap,String id) {
        this.tag = tag;
        this.person = person;
        this.bitmap = bitmap;
        this.id=id;
    }

    public String getTag() {
        return tag;
    }

    public String getPerson() {
        return person;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getId() {
        return id;
    }
}

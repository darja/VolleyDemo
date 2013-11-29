package com.demos.volley.model.mediawiki;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "Item", strict = false)
public class SearchResult {
    @Path(value = "Image")
    @Attribute(name = "source")
    private String mImageUrl;

    @Element(name = "Text")
    private String mTitle;

    @Element(name = "Description", required = false)
    private String mDescription;

    @Element(name = "Url")
    private String mUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }
}

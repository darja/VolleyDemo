package com.demos.volley.model.mediawiki;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class SearchSuggestion {
    @ElementList(inline = false, name = "Section")
    private List<SearchResult> mSearchResults;

    public List<SearchResult> getSearchResults() {
        return mSearchResults;
    }
}

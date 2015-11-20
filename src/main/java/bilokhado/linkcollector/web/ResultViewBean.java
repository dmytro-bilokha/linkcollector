package bilokhado.linkcollector.web;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ResultViewBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String searchQuery;
	private TagsList tags;

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public TagsList getTags() {
		return tags;
	}

	public void setTags(TagsList tags) {
		this.tags = tags;
	}

}

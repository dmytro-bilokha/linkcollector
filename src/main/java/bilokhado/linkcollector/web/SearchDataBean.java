package bilokhado.linkcollector.web;

import java.io.Serializable;

import bilokhado.linkcollector.ejb.ConfigBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;

@Named
@SessionScoped
public class SearchDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String searchQuery;
	private String tagText;
	private Integer tagWeight;
	@Inject
	private TagsList tags;

	public String addTag() {
		tags.add(new QueryTag(tagText, tagWeight));
		return "index";
	}

	public String removeTag(QueryTag tag) {
		tags.remove(tag);
		return "index";
	}

	public String editTag(QueryTag tag) {
		tagText = tag.getTagText();
		tagWeight = tag.getTagWeight();
		tags.remove(tag);
		return "index";
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public Integer getTagWeight() {
		return tagWeight;
	}

	public void setTagWeight(Integer tagWeight) {
		this.tagWeight = tagWeight;
	}

	public TagsList getTags() {
		return tags;
	}

}

package bilokhado.linkcollector.web;

import java.io.Serializable;
import java.util.Map;

import bilokhado.linkcollector.ejb.ConfigBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Named
@SessionScoped
public class SearchDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Size(min=1, message="Please, enter search query")
	private String searchQuery;
	private String tagText;
	@Min(value=-999, message="Weight must be above -1000")
	@Max(value=9999, message="Weight must be below 10000")
	private Integer tagWeight;
	@Inject
	private TagsList tags;
	private QueryTag tagEdited = null;
	
	public void addTag(ActionEvent evt) {
		tags.add(new QueryTag(tagText, tagWeight));
		return;
	}
	
	public String removeTag(QueryTag tag) {
		tags.remove(tag);
		return "index";
	}

	public String editTag(QueryTag tag) {
		tagEdited = tag;
		return "index";
	}

	public String updateTag() {
		tagEdited = null;
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
	
	public QueryTag getTagEdited() {
		return tagEdited;
	}
			
}

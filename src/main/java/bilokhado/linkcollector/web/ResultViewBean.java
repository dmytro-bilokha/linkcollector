package bilokhado.linkcollector.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import bilokhado.linkcollector.ejb.SearcherBean;
import bilokhado.linkcollector.entity.ScoringResult;

@Named
@RequestScoped
public class ResultViewBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String searchQuery;
	private TagsList tags;
	private List<ScoringResult> searchResult;

	@EJB
	SearcherBean searcher;

	public void onload() {
		try {
			searchResult = searcher.search(searchQuery, tags);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			// TODO: add code for dialing with searchResult == null
		}
	}

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

	public List<ScoringResult> getSearchResult() {
		return searchResult;
	}

}

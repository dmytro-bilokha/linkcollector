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
import bilokhado.linkcollector.entity.TagsList;

/**
 * A backing bean for the results page.
 */
@Named
@RequestScoped
public class ResultViewBean implements Serializable {

	/**
	 * Version for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Search query string.
	 */
	private String searchQuery;

	/**
	 * Tags list.
	 */
	private TagsList tags;

	/**
	 * The list of the scoring results.
	 */
	private List<ScoringResult> searchResult;

	/**
	 * Flag indicating page loading. Used to display loading image.
	 */
	private boolean loaded = false;

	/**
	 * Reference to EJB for search and score business logic.
	 */
	@EJB
	SearcherBean searcher;

	/**
	 * Calls search and score business logic method and stores results for
	 * showing on web page.
	 */
	public void onload() {
		try {
			searchResult = searcher.search(searchQuery, tags);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		loaded = true;
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

	public boolean isLoaded() {
		return loaded;
	}

}

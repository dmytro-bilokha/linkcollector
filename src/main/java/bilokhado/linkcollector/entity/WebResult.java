package bilokhado.linkcollector.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A class representing result of web search (web page).
 */
@Entity
@Table(name = "WEB_RESULT")
@NamedQuery(name = "WebResult.findByQueryHash", query = "SELECT wr FROM WebResult wr WHERE wr.searchQuery.queryHash = :hash")
public class WebResult implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "QUERY_HASH")
	private SearchQuery searchQuery;
	@Column(name = "TITLE")
	private String title;
	@Column(name = "URL")
	private String url;
	@Column(name = "DISPLAY_URL")
	private String displayUrl;
	@Column(name = "DESCRIPTION")
	private String description;

	public WebResult() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SearchQuery getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
	}

	public WebResult(SearchQuery searchQuery, String title, String url,
			String displayUrl, String description) {
		this.searchQuery = searchQuery;
		this.title = title;
		this.url = url;
		this.description = description;
		setDisplayUrl(url);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl.length() <= 45 ? displayUrl : displayUrl
				.substring(0, 45) + "...";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime
				+ ((searchQuery == null) ? 0 : searchQuery.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebResult other = (WebResult) obj;
		if (searchQuery == null) {
			if (other.searchQuery != null)
				return false;
		} else if (!searchQuery.equals(other.searchQuery))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebResult [id=" + id + ", searchQuery=" + searchQuery + "]";
	}

}

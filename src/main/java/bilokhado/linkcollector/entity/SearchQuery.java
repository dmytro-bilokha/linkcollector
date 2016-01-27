package bilokhado.linkcollector.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A class representing search query.
 */
@Entity
@Table(name = "SEARCH_QUERY")
@NamedNativeQuery(name = "SearchQuery.deleteOutdated", query = "DELETE FROM SEARCH_QUERY WHERE TIME_PERSIST < SUBDATE(NOW(), INTERVAL ? HOUR);")
public class SearchQuery implements Serializable, Comparable<SearchQuery> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "QUERY_HASH", updatable = false, nullable = false)
	private long queryHash;
	@Column(name = "TIME_PERSIST", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	public SearchQuery() {
	}

	public SearchQuery(long queryHash) {
		this.queryHash = queryHash;
	}

	public long getQueryHash() {
		return queryHash;
	}

	public void setQueryHash(long queryHash) {
		this.queryHash = queryHash;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int compareTo(SearchQuery other) {
		int comparingResult;
		if ((comparingResult = timeStamp.compareTo(other.timeStamp)) != 0)
			return comparingResult;
		return Long.compare(queryHash, other.queryHash);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + (int) (queryHash ^ (queryHash >>> 32));
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		SearchQuery other = (SearchQuery) obj;
		if (queryHash != other.queryHash)
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SearchQuery [queryHash=" + queryHash + "]";
	}

}

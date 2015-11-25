package bilokhado.linkcollector.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="SEARCH_QUERY")
public class SearchQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="QUERY_HASH", updatable = false, nullable = false)
	private long queryHash;
	@Column(name="TIME_PERSIST", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)		
	private Date timeStamp;
	
	public SearchQuery() {}
	
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
}

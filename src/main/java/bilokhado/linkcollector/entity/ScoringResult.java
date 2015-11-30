package bilokhado.linkcollector.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SCORING_RESULT")
@NamedQuery(name = "ScoringResult.findByTagsHash", query = "SELECT sr FROM ScoringResult sr WHERE sr.tagsHash = :hash ORDER BY sr.score DESC")
public class ScoringResult implements Serializable, Comparable<ScoringResult> {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "TAGS_HASH")
	private long tagsHash;
	@Id
	@ManyToOne
	@JoinColumn(name = "WEB_RESULT_ID")
	private WebResult scoredWebResult;
	@Column(name = "SCORE")
	private int score;

	public ScoringResult() {
	}

	public ScoringResult(WebResult scoredWebResult, int score) {
		this.scoredWebResult = scoredWebResult;
		this.score = score;
	}

	public long getTagsHash() {
		return tagsHash;
	}

	public void setTagsHash(long tagsHash) {
		this.tagsHash = tagsHash;
	}

	public WebResult getScoredWebResult() {
		return scoredWebResult;
	}

	public void setScoredWebResult(WebResult scoredWebResult) {
		this.scoredWebResult = scoredWebResult;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(ScoringResult other) {
		if (this.score > other.getScore())
			return -1;
		else
			return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + score;
		result = prime * result
				+ ((scoredWebResult == null) ? 0 : scoredWebResult.hashCode());
		result = prime * result + (int) (tagsHash ^ (tagsHash >>> 32));
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
		ScoringResult other = (ScoringResult) obj;
		if (tagsHash != other.tagsHash)
			return false;
		if (score != other.score)
			return false;
		if (scoredWebResult == null) {
			if (other.scoredWebResult != null)
				return false;
		} else if (!scoredWebResult.equals(other.scoredWebResult))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScoringResult [tagsHash=" + tagsHash + "]";
	}

}

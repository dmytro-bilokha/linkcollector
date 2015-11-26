package bilokhado.linkcollector.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SCORING_RESULT")
public class ScoringResult implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private long tagsHash;
	@Id
	@ManyToOne
	@JoinColumn(name = "WEB_RESULT_ID")
	private WebResult scoredWebResult;
	private int score;
}

package bilokhado.linkcollector.web;

import javax.inject.Named;

public class QueryTag {

	private String tagText;
	private int tagWeight;

	public QueryTag(String tagText, int tagWeight) {
		this.tagText = tagText;
		this.tagWeight = tagWeight;
	}
	
	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public int getTagWeight() {
		return tagWeight;
	}

	public void setTagWeight(int tagWeight) {
		this.tagWeight = tagWeight;
	}

}

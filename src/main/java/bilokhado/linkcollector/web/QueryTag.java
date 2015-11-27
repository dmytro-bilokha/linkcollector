package bilokhado.linkcollector.web;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tagText == null) ? 0 : tagText.hashCode());
		result = prime * result + tagWeight;
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
		QueryTag other = (QueryTag) obj;
		if (tagText == null) {
			if (other.tagText != null)
				return false;
		} else if (!tagText.equals(other.tagText))
			return false;
		if (tagWeight != other.tagWeight)
			return false;
		return true;
	}
	
}

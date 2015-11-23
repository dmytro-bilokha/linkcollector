package bilokhado.linkcollector.entity;

public class WebResult {
	private String title;
	private String url;
	private String displayUrl;
	private String description;
	
	public WebResult() {}
	
	public WebResult(String title, String url, String displayUrl, String description) {
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
		this.displayUrl = displayUrl.length() <= 45 ? displayUrl : displayUrl.substring(0, 45) + "...";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

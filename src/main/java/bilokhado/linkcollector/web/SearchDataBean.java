package bilokhado.linkcollector.web;

import java.io.Serializable;

import bilokhado.linkcollector.ejb.ConfigBean;
import bilokhado.linkcollector.entity.QueryTag;
import bilokhado.linkcollector.entity.TagsList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;

@Named
@ConversationScoped
public class SearchDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	private Conversation conversation;
	@EJB
	private ConfigBean config;

	private String vpSearchQuery;
	private TagsList vpTagsList;
	private String searchQuery;
	private String tagText;
	private Integer tagWeight;
	@Inject
	private TagsList tags;
	private QueryTag tagEdited = null;

	@PostConstruct
	private void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * Long.parseLong(config
					.getConfigValue("ConversationTimeout")));
		}
	}
	
	public void onload() {
		if(vpSearchQuery != null)
			searchQuery = vpSearchQuery;
		if(vpTagsList != null)
			tags = vpTagsList;
	}

	public String gotoSearch() {
		if(!conversation.isTransient())
			conversation.end();
		//return "processing?faces-redirect=true";
		return "result?faces-redirect=true&amp;q=" + searchQuery + "&amp;tags="
				+ tags.toString();
	}

	public void addTag() {
		tags.add(new QueryTag(tagText, tagWeight));
		tagWeight = null;
		tagText = null;
	}

	public void removeTag(QueryTag tag) {
		tags.remove(tag);
	}

	public void editTag(QueryTag tag) {
		tagEdited = tag;
	}

	public void updateTag() {
		tagEdited = null;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public Integer getTagWeight() {
		return tagWeight;
	}

	public void setTagWeight(Integer tagWeight) {
		this.tagWeight = tagWeight;
	}

	public TagsList getTags() {
		return tags;
	}

	public QueryTag getTagEdited() {
		return tagEdited;
	}

	public String getConversationId() {
		return conversation.getId();
	}

	public String getVpSearchQuery() {
		return vpSearchQuery;
	}

	public void setVpSearchQuery(String vpSearchQuery) {
		this.vpSearchQuery = vpSearchQuery;
	}

	public TagsList getVpTagsList() {
		return vpTagsList;
	}

	public void setVpTagsList(TagsList vpTagsList) {
		this.vpTagsList = vpTagsList;
	}
	
}

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

/**
 * A backing bean for home page.
 */
@Named
@ConversationScoped
public class SearchDataBean implements Serializable {
	
	/**
	 * Version for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Conversation reference.
	 */
	@Inject
	private Conversation conversation;

	/**
	 * Reference to {@code ConfigBean} for configuration reading.
	 */
	@EJB
	private ConfigBean config;

	/**
	 * Search query string from viewparam.
	 */
	private String vpSearchQuery;

	/**
	 * Tags list from viewparam.
	 */
	private TagsList vpTagsList;

	/**
	 * Search query string.
	 */
	private String searchQuery;

	/**
	 * Text of the tag to add into the tags table.
	 */
	private String tagText;

	/**
	 * Weight of the tag to add into the tags table.
	 */
	private Integer tagWeight;

	/**
	 * Tags list.
	 */
	@Inject
	private TagsList tags;

	/**
	 * Reference to edited tag.
	 */
	private QueryTag tagEdited = null;

	/**
	 * Begins conversation and sets conversation timeout.
	 */
	@PostConstruct
	private void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * Long.parseLong(config
					.getConfigValue("ConversationTimeout")));
		}
	}

	/**
	 * Called before page is shown. Sets data from viewparams.
	 */
	public void onload() {
		if (vpSearchQuery != null)
			searchQuery = vpSearchQuery;
		if (vpTagsList != null)
			tags = vpTagsList;
	}

	/**
	 * Finishes conversation and navigates to results page.
	 * 
	 * @return the string with link to results page and viewparams for it.
	 */
	public String gotoSearch() {
		if (!conversation.isTransient())
			conversation.end();
		return "result?faces-redirect=true&amp;q=" + searchQuery + "&amp;tags="
				+ tags.toString();
	}

	/**
	 * Adds tag to the list of tags.
	 */
	public void addTag() {
		tags.add(new QueryTag(tagText, tagWeight));
		tagWeight = null;
		tagText = null;
	}

	/**
	 * Removes the tag from the list of tags.
	 * 
	 * @param tag
	 *            the tag to remove from list.
	 */
	public void removeTag(QueryTag tag) {
		tags.remove(tag);
	}

	/**
	 * Sets reference to the tag to edit.
	 * 
	 * @param tag
	 *            the tag to edit.
	 */
	public void editTag(QueryTag tag) {
		tagEdited = tag;
	}

	/**
	 * Ends editing the tag via setting reference to null.
	 */
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

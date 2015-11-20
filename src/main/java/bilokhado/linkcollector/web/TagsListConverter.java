package bilokhado.linkcollector.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = TagsList.class)
public class TagsListConverter implements Converter {
	private TagsList tags = new TagsList();

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String tagsEncodedJson) {
		try {
			tags.populateFromUrl(tagsEncodedJson);
			return tags;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			throw new ConverterException(message);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object tagsList) {
		// This converter is for viewParams, so conversion object -> string is
		// not needed
		return "";
	}

}

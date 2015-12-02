package bilokhado.linkcollector.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import bilokhado.linkcollector.entity.TagsList;

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
		if (tagsList instanceof TagsList) {
			return ((TagsList) tagsList).toString();
		}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Unsupported object type to convert",
				"Unsupported object type to convert");
		throw new ConverterException(message);
	}

}

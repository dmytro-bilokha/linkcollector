package bilokhado.linkcollector.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import bilokhado.linkcollector.entity.TagsList;

/**
 * A JSF converter for {@code TagsList} class.
 *
 */
@FacesConverter(forClass = TagsList.class)
public class TagsListConverter implements Converter {

	/**
	 * Converts {@code String} to {@code TagsList}.
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String tagsJson) {
		TagsList tags = new TagsList();
		try {
			tags.populateFromJson(tagsJson);
			return tags;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			throw new ConverterException(message);
		}
	}

	/**
	 * Converts {@code TagsList} {@code String}.
	 * 
	 * @see bilokhado.linkcollector.entity.TagsList#toString()
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object tagsList) {
		if (tagsList instanceof TagsList) {
			return ((TagsList) tagsList).toString();
		}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unsupported object type to convert",
				"Unsupported object type to convert");
		throw new ConverterException(message);
	}

}

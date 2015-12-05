package bilokhado.linkcollector.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * A JSF custom validator class. Used to validate query tag in home page.
 */
@FacesValidator("tagInValidator")
public class TagInValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		Integer weight = (Integer) value;
		UIInput tagTextComponent = (UIInput) component.getAttributes().get(
				"tagTextComponent");
		String tagText = (String) tagTextComponent.getValue();
		if (weight == null || tagText == null || tagText.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Tag\'s text pattern and weight are required", null);
			throw new ValidatorException(msg);
		}
		if (weight < -999 || weight > 9999) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Weight must be from -999 to 9999", null);
			throw new ValidatorException(msg);
		}
		return;
	}

}

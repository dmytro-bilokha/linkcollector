package bilokhado.linkcollector.web;

import bilokhado.linkcollector.ejb.ConfigBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ejb.EJB;

@Named
@RequestScoped
public class SearchDataBean {
	@EJB private ConfigBean conf;
	
	public String getDemoString() {
		return conf.getConfigValue("AzureKey");
	}
}

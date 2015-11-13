package bilokhado.linkcollector.web;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.Dependent;

@Dependent
public class TagsList implements Serializable {
	private static final long serialVersionUID = 1L;
	private final List<QueryTag> tags = new LinkedList<>();

	public List<QueryTag> getTags() {
		return tags;
	}

	public void add(QueryTag tag) {
		tags.add(0, tag);
	}

	public void remove(QueryTag tag) {
		tags.remove(tag);
	}
}

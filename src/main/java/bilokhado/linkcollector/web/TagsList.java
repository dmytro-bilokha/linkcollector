package bilokhado.linkcollector.web;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

@Dependent
public class TagsList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.web.TagsList");

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

	private void normalize() {
		Map<String, Boolean> seen = new HashMap<>();
		ListIterator<QueryTag> iterator = tags.listIterator(tags.size());
		while (iterator.hasPrevious()) {
			QueryTag t = iterator.previous();
			t.setTagText(t.getTagText().toLowerCase());
			if (seen.putIfAbsent(t.getTagText(), Boolean.TRUE) != null
					|| t.getTagWeight() == 0) {
				iterator.remove();
			}
		}
	}

	public long calculateHash() {
		long hash = 1;
		for (QueryTag t : tags)
			hash = 31 * hash + t.hashCode();
		return hash;
	}

	public void populateFromUrl(String encodedJsonData) throws Exception {
		String jsonData = null;
		try {
			jsonData = URLDecoder.decode(encodedJsonData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Unable to decode JSON from URL: "
					+ encodedJsonData);
			throw new Exception("Unable to decode JSON from URL: "
					+ encodedJsonData);
		}
		try (JsonParser parser = Json.createParser(new StringReader(jsonData))) {
			String key = null;
			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				switch (event) {
				case KEY_NAME:
					key = parser.getString();
					break;

				case VALUE_NUMBER:
					add(new QueryTag(key, parser.getInt()));
					break;

				case START_OBJECT:
				case END_OBJECT:
					break;

				default:
					logger.log(Level.SEVERE,
							"Unsupported tag passed in JSON string: "
									+ jsonData);
					break;
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error happen while parsing JSON string: "
					+ jsonData);
			throw new Exception("Unable to decode tags list from URL");
		}
		normalize();
	}

	@Override
	public String toString() {
		StringWriter buffer = new StringWriter();
		try (JsonGenerator jgen = Json.createGenerator(buffer)) {
			jgen.writeStartObject();
			tags.forEach(t -> jgen.write(t.getTagText(), t.getTagWeight()));
			jgen.writeEnd();
			jgen.flush();
			return URLEncoder.encode(buffer.toString(), "UTF-8");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to encode URL with list of tags: "
					+ buffer.toString());
			return null;
		}
	}

}

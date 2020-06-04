package MapReduce;

import java.util.List;

public class EmissionTemplate {

    EmissionTemplate(String Content, List<String> Links) {
        emissionKey = Content;
        emissionValue = Links;
    }

    // GETTERS
    public List<String> getValue() {
        return emissionValue;
    }
    public String getKey() {
        return emissionKey;
    }

    // FIELDS
    private final List<String> emissionValue;
    private final String emissionKey;
}

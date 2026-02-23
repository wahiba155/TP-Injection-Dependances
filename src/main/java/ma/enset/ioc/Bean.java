package ma.enset.ioc;

import java.util.List;
import jakarta.xml.bind.annotation.*;
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String className; // on changera pour "class" car "class" est mot réservé

    @XmlElement(name = "property")
    private List<Property> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "class")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
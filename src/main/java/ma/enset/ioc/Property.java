package ma.enset.ioc;

import jakarta.xml.bind.annotation.XmlAttribute;
public class Property {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
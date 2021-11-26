package apidoc;

import java.util.Objects;

// api字段实体类
public class ApiField {
    public String name;
    public String desc;
    public String in;
    public String type;
    public String schema;

    public ApiField() {
    }

    public ApiField(String name, String desc, String type, String schema) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.schema = schema;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiField apiField = (ApiField) o;
        if (!Objects.equals(name, apiField.name)) {
            return false;
        }
        return Objects.equals(type, apiField.type);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

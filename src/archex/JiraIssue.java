package archex;

public class JiraIssue {

    private Fields fields;

    public static class Fields {
        private Parent parent;
        private Project project;
        private String summary;
        private Issuetype issuetype;
        private Assignee assignee;
        private Reporter reporter;
        private Priority priority;
        private String customfield_10607;
        private String customfield_10609;
        private Float customfield_10006;

        public Parent getParent() {
            return parent;
        }

        public void setParent(Parent parent) {
            this.parent = parent;
        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public Issuetype getIssuetype() {
            return issuetype;
        }

        public void setIssuetype(Issuetype issuetype) {
            this.issuetype = issuetype;
        }

        public Assignee getAssignee() {
            return assignee;
        }

        public void setAssignee(Assignee assignee) {
            this.assignee = assignee;
        }

        public Reporter getReporter() {
            return reporter;
        }

        public void setReporter(Reporter reporter) {
            this.reporter = reporter;
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public String getCustomfield_10607() {
            return customfield_10607;
        }

        public void setCustomfield_10607(String customfield_10607) {
            this.customfield_10607 = customfield_10607;
        }

        public String getCustomfield_10609() {
            return customfield_10609;
        }

        public void setCustomfield_10609(String customfield_10609) {
            this.customfield_10609 = customfield_10609;
        }

        public Float getCustomfield_10006() {
            return customfield_10006;
        }

        public void setCustomfield_10006(Float customfield_10006) {
            this.customfield_10006 = customfield_10006;
        }
    }

    public static class Parent {
        private String key;

        public Parent(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class Project {
        private String id;

        public Project(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Issuetype {
        private String id;

        public Issuetype(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Assignee {
        private String name;

        public Assignee(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Reporter {
        private String name;

        public Reporter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Priority {
        private String id;

        public Priority(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

}

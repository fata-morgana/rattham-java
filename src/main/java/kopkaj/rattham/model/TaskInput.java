package kopkaj.rattham.model;

public class TaskInput {
	private String subject;
	private String content;
	public TaskInput() {
		//default, needed by jackson
	}
	public TaskInput(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public String getContent() {
		return content;
	}
}

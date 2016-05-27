package kopkaj.rattham.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kopkaj.rattham.exception.RatthamException;

public class Task {
	private final String subject;
	private final String content;
	private final TaskStatus taskStatus;
	
	public Task(TaskInput taskInput) {
		this.subject = taskInput.getSubject();
		this.content = taskInput.getContent();
		this.taskStatus = TaskStatus.PENDING;
		validateLength();
	}
	
	public Task(String line) {
		String [] decodedFields = decode(line).split(";");
		if (decodedFields.length < 3) {
			throw new RatthamException("Data corrupt, fields were shrink. Original data is " + line);
		}
		this.subject = decodedFields[0];
		this.content = decodedFields[1];
		this.taskStatus = TaskStatus.parse(decodedFields[2].toUpperCase());
		validateLength();
	}
	
	private void validateLength() {
		List<String> errorMsg = new ArrayList<>();
		if (subject.length() > 40) {
			errorMsg.add("Subject's length must be less than 40.");
		}
		if (content.length() > 255) {
			errorMsg.add("Content's length must be less than 255.");
		}
		if (!errorMsg.isEmpty()) {
			throw new RatthamException(Arrays.deepToString(errorMsg.toArray(new String[0])));
		}
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	
	public String toString() {
		return encode(subject)+";"+encode(content)+";"+taskStatus.toString();
	}
	
	public static String encode(String field) {
		List<String> encodedChar = IntStream.range(0, field.length())
			.mapToObj(i -> new Character(field.charAt(i)).toString())
			.map(oneChar -> oneChar.equals(";") ? "\\;" : (oneChar.equals("\\") ? "\\\\" : oneChar))
			.collect(Collectors.toList());
		StringBuilder sb = new StringBuilder();
		encodedChar.stream().forEach(one -> sb.append(one));
		return sb.toString();
	}
	
	public static String decode(String line) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < line.length(); i++) {
			char testingChar = line.charAt(i);
			if (testingChar != '\\') {
				sb.append(testingChar);
			} else {
				String testingString = new String(new char[] {testingChar, line.charAt(++i)});
				if (testingString.equals("\\\\")) {
					sb.append("\\");
				} else if (testingString.equals("\\;")) {
					sb.append(";");
				} else {
					throw new RatthamException("Data corrupt. Original data is " + line);
				}
			}
		}
		return sb.toString();
	}
}

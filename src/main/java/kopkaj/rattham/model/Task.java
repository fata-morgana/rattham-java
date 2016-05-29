package kopkaj.rattham.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kopkaj.rattham.exception.RatthamException;

public class Task {
	private static final String FIELD_SEPARATOR = ";";
	private Integer lineNo;
	private final String subject;
	private final String content;
	private TaskStatus taskStatus;
	
	public Task(TaskInput taskInput) {
		this.lineNo = -1;
		this.subject = taskInput.getSubject();
		this.content = taskInput.getContent();
		this.taskStatus = TaskStatus.PENDING;
		validateLength();
	}
	
	public Task(String line) {
		String [] decodedFields = decode(line).split(FIELD_SEPARATOR);
		if (decodedFields.length < 4) {
			throw new RatthamException("Data corrupt, fields were shrink. Original data is " + line);
		}
		this.lineNo = Integer.parseInt(decodedFields[0]);
		this.subject = decodedFields[1];
		this.content = decodedFields[2];
		this.taskStatus = TaskStatus.parse(decodedFields[3].toUpperCase());
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

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	
	public String toString() {
		return lineNo+FIELD_SEPARATOR+encode(subject)+FIELD_SEPARATOR+encode(content)+FIELD_SEPARATOR+taskStatus.toString();
	}
	
	public static String encode(String field) {
		List<String> encodedChar = IntStream.range(0, field.length())
			.mapToObj(i -> new Character(field.charAt(i)).toString())
			.map(oneChar -> oneChar.equals(FIELD_SEPARATOR) ? "\\;" : (oneChar.equals("\\") ? "\\\\" : oneChar))
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
					sb.append(FIELD_SEPARATOR);
				} else {
					throw new RatthamException("Data corrupt. Original data is " + line);
				}
			}
		}
		return sb.toString();
	}
}

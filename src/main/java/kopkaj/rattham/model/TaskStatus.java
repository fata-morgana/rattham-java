package kopkaj.rattham.model;

import kopkaj.rattham.exception.RatthamException;

public enum TaskStatus {
	PENDING, DONE, DELETED;
	
	public static TaskStatus parse(String taskStatus) {
		switch(taskStatus) {
			case "PENDING" : return PENDING;
			case "DONE" : return DONE;
			case "DELETED" : return DELETED;
			default: throw new RatthamException("Unknown task status:" + taskStatus);
		}
	}
}

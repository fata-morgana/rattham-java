package kopkaj.rattham.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kopkaj.rattham.exception.RatthamDataException;
import kopkaj.rattham.exception.RatthamException;
import kopkaj.rattham.model.Task;
import kopkaj.rattham.model.TaskStatus;

import org.springframework.stereotype.Service;

@Service
public class TaskManager {
	private static Path storagePath() {
		return Paths.get("storage");
	}
	
	public List<Task> readAll() {
		return readWithCondition(task -> true);
	}
	
	public List<Task> readAllAvailable() {
		return readWithCondition(task -> task.getTaskStatus() != TaskStatus.DELETED);
	}
	
	private List<Task> readWithCondition(Predicate<Task> condition) {
		File storageFile = storagePath().toFile();
		if (!storageFile.exists()) {
			return Collections.emptyList();
		}
		try (Stream<String> stream = Files.lines(storagePath())) {
			return stream.filter(line -> !line.isEmpty())
				.map(line -> new Task(line))
				.filter(condition)
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RatthamException("Error while reading tasks.", e);
		}
	}
	
	public Task read(int line) {
		List<Task> allTasks = readAll();
		if (notApplicableTask(line, allTasks)) {
			throw new RatthamDataException(String.format("Task no. %d not found or deleted.", line));
		}
		
		return allTasks.get(line);
	}
	
	public Integer add(Task task) {
		try {
			Integer lineNo = readAllAvailable().size();
			task.setLineNo(lineNo);
			Files.write(storagePath(), 
					(task.toString() + System.getProperty("line.separator")).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RatthamException("Cannot insert task.", e);
		}
		
		return readAll().size();
	}
	
	public void edit(int line, Task task) {
		List<Task> allTasks = readAll();
		if (!isValidToEdit(allTasks, line, taskStatus -> taskStatus == TaskStatus.PENDING)) {
			throw new RatthamDataException("The specified task is not available for edit.");
		}
		editTaskByLine(line, task, allTasks);
	}
	
	public void changeStatusToDone(int line) {
		List<Task> allTasks = readAll();
		if (!isValidToEdit(allTasks, line, taskStatus -> taskStatus == TaskStatus.PENDING)) {
			throw new RatthamDataException("The specified task can't be move to done.");
		}
		Task modTask = allTasks.get(line);
		modTask.setTaskStatus(TaskStatus.DONE);
		editTaskByLine(line, modTask, allTasks);
	}
	
	public void markInactive(int line) {
		List<Task> allTasks = readAll();
		if (!isValidToEdit(allTasks, line, taskStatus -> taskStatus != TaskStatus.DELETED)) {
			throw new RatthamDataException("The specified task is not available for deleted.");
		}
		Task modTask = allTasks.get(line);
		modTask.setTaskStatus(TaskStatus.DELETED);
		editTaskByLine(line, modTask, allTasks);
	}
	
	private boolean isValidToEdit(List<Task> allTasks, int line, FilteredTaskStatus filtered) {
		TaskStatus taskStatus = allTasks.get(line).getTaskStatus();
		return allTasks.size() < line || filtered.apply(taskStatus) ;
	}
	
	private void editTaskByLine(int line, Task edittedTask, List<Task> allTasks) {
		allTasks.remove(line);
		allTasks.add(line, edittedTask);
		try {
			writeAllLines(allTasks);
		} catch (IOException e) {
			throw new RatthamException("Error while accessing storage.", e);
		}
		
	}

	private void writeAllLines(List<Task> allTasks) throws IOException {
		Files.write(storagePath(), 
				(Iterable<String>) allTasks.stream().map(oneTask -> oneTask.toString())::iterator,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private boolean notApplicableTask(int line, List<Task> allTasks) {
		return allTasks.size() <= line || allTasks.get(line).getTaskStatus() == TaskStatus.DELETED;
	}
	
	private static interface FilteredTaskStatus extends Function<TaskStatus, Boolean> {}
}

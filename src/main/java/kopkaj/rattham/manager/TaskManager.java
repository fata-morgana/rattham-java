package kopkaj.rattham.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
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
		File storageFile = storagePath().toFile();
		if (!storageFile.exists()) {
			return Collections.emptyList();
		}
		try (Stream<String> stream = Files.lines(storagePath())) {
			return stream.filter(line -> !line.isEmpty())
				.map(line -> new Task(line))
				.filter(task -> task.getTaskStatus() != TaskStatus.DELETED)
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
	
	public void add(Task task) {
		try {
			Files.write(storagePath(), 
					(task.toString() + System.getProperty("line.separator")).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RatthamException("Cannot insert task.", e);
		}
	}
	
	public void edit(int line, Task task) {
		List<Task> allTasks = readAll();
		if (notApplicableTask(line, allTasks)) {
			throw new RatthamDataException("The specified task is not available for edit.");
		}
		allTasks.remove(line);
		allTasks.add(line, task);
		
	}

	private boolean notApplicableTask(int line, List<Task> allTasks) {
		return allTasks.size() <= line || allTasks.get(line).getTaskStatus() == TaskStatus.DELETED;
	}
}

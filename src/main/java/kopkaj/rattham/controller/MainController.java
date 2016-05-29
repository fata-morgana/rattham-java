package kopkaj.rattham.controller;

import java.util.List;

import kopkaj.rattham.manager.TaskManager;
import kopkaj.rattham.model.Task;
import kopkaj.rattham.model.TaskInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/rattham")
public class MainController {
	@Autowired
	private TaskManager taskManager;
	
	@ResponseBody
	@RequestMapping(path="/task", method=RequestMethod.GET)
	public List<Task> listTask() {
		return taskManager.readAllAvailable();
	}
	
	@ResponseBody
	@RequestMapping(path="/task/{taskNo}", method=RequestMethod.GET)
	public Task viewTask(@PathVariable Integer taskNo) {
		return taskManager.read(taskNo);
	}
	
	@RequestMapping(path="/task", method=RequestMethod.POST)
	public ResponseEntity<Integer> addTask(@RequestBody TaskInput taskInput) {
		Integer newLine = taskManager.add(new Task(taskInput));
		return new ResponseEntity<>(newLine, HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/task/{taskNo}", method=RequestMethod.PUT)
	public ResponseEntity<?> editTask(@PathVariable Integer taskNo, @RequestBody TaskInput taskInput) {
		Task modTask = new Task(taskInput);
		modTask.setLineNo(taskNo);
		taskManager.edit(taskNo, modTask);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(path="/task/{taskNo}/done", method=RequestMethod.GET)
	public ResponseEntity<?> moveToDone(@PathVariable Integer taskNo) {
		taskManager.changeStatusToDone(taskNo);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(path="/task/{taskNo}", method=RequestMethod.DELETE)
	public ResponseEntity<?> removeTask(@PathVariable Integer taskNo) {
		taskManager.markInactive(taskNo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

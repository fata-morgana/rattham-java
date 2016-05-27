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
		return taskManager.readAll();
	}
	
	@ResponseBody
	@RequestMapping(path="/task/{task}", method=RequestMethod.GET)
	public Task viewTask(@PathVariable String task) {
		return taskManager.read(Integer.parseInt(task));
	}
	
	@RequestMapping(path="/task", method=RequestMethod.POST)
	public ResponseEntity<?> addTask(@RequestBody TaskInput taskInput) {
		taskManager.add(new Task(taskInput));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/task/{task}", method=RequestMethod.PUT)
	public ResponseEntity<?> editTask(@PathVariable String task, @RequestBody TaskInput taskInput) {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

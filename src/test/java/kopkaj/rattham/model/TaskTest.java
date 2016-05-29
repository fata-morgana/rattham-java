package kopkaj.rattham.model;

import kopkaj.rattham.exception.RatthamException;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TaskTest {
	@Test
	public void testEncode() {
		assertThat("0\\;1\\;2\\\\3\\\\\\;", equalTo(Task.encode("0;1;2\\3\\;")));
	}
	@Test
	public void testDecode() {
		assertThat("0;1;2\\3\\;", equalTo(Task.decode("0\\;1\\;2\\\\3\\\\\\;")));
	}
	@Test(expected=RatthamException.class)
	public void testDecodeFail() {
		Task.decode("\\x");
	}
	@Test
	public void testParse() {
		Task task = new Task("0\\;sub\\;con\\;PENDING");
		assertThat(0, equalTo(task.getLineNo()));
		assertThat("sub", equalTo(task.getSubject()));
		assertThat("con", equalTo(task.getContent()));
		assertThat(TaskStatus.PENDING, equalTo(task.getTaskStatus()));
	}
}

package kopkaj.rattham.model;

import kopkaj.rattham.exception.RatthamException;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TaskTest {
	@Test
	public void testEncode() {
		assertThat("1\\;2\\\\3\\\\\\;", equalTo(Task.encode("1;2\\3\\;")));
	}
	@Test
	public void testDecode() {
		assertThat("1;2\\3\\;", equalTo(Task.decode("1\\;2\\\\3\\\\\\;")));
	}
	@Test(expected=RatthamException.class)
	public void testDecodeFail() {
		Task.decode("\\x");
	}
	@Test
	public void testParse() {
		Task task = new Task("sub\\;con\\;PENDING");
		assertThat("sub", equalTo(task.getSubject()));
		assertThat("con", equalTo(task.getContent()));
		assertThat("PENDING", equalTo(task.getTaskStatus()));
	}
}

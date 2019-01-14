package client;

import java.io.Serializable;

public class TestMsg  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String msg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public TestMsg(Integer id, String msg) {
		super();
		this.id = id;
		this.msg = msg;
	}
	
	
	public TestMsg() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "TestMsg [id=" + id + ", msg=" + msg + "]";
	}

}

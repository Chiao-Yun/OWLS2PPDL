package converter;
//test
public class IOPE_Object {

	private String type;
	private String ID;
	
	public IOPE_Object(String Type, String iD) {
		type = Type;
		ID = iD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
}
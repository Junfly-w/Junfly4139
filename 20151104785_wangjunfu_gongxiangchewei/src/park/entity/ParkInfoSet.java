package park.entity;

public class ParkInfoSet {
	private Integer id;    		//�û�id
	private String parkName;//ͣ��������
	private String parkLocation;//ͣ������ַ
	private String openDate;//����ʱ��
	private String closeDate;//����ʱ��
	private int parkNumber;//ͣ����ͣ��λ����
	private int rentOrSale;//0��ʾ�⣬1��ʾ����
	private double feeScale;//���
	private String userInfo;//ʹ����Ϣ
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getParkLocation() {
		return parkLocation;
	}
	public void setParkLocation(String parkLocation) {
		this.parkLocation = parkLocation;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public int getRentOrSale() {
		return rentOrSale;
	}
	public void setRentOrSale(int rentOrSale) {
		this.rentOrSale = rentOrSale;
	}
	public double getFeeScale() {
		return feeScale;
	}
	public void setFeeScale(double feeScale) {
		this.feeScale = feeScale;
	}
	public int getParkNumber() {
		return parkNumber;
	}
	public void setParkNumber(int parkNumber) {
		this.parkNumber = parkNumber;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	
}

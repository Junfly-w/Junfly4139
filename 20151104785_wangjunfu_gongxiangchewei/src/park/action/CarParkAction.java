package park.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import park.daoImp.CarParkInfoDaoImp;
import park.daoImp.ChargeInfoDaoImp;
import park.daoImp.ParkInfoSetDaoImp;
import park.daoImp.ReservationDaoImp;
import park.entity.CarParkInfo;
import park.entity.ChargeInfo;
import park.entity.ParkInfoSet;
import park.entity.Reservation;
import park.util.Constants;
import park.util.Dumper;
import park.util.MyDate;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

public class CarParkAction extends ActionSupport {
	private CarParkInfo carParkInfo;
	private ParkInfoSet parkInfoSet;
	private List<String> parkList = new ArrayList<String>();
	private List<String> locationList = new ArrayList<String>();
	private CarParkInfoDaoImp carParkInfoDaoImp = new CarParkInfoDaoImp();
	private ParkInfoSetDaoImp parkInfoSetDaoImp = new ParkInfoSetDaoImp();
	private ReservationDaoImp reservationDaoImp = new ReservationDaoImp();
	private String para;
	private String id;
	private String name;
	private String type;
	private Integer count;
	private String carNo;
	private List<CarParkInfo> listData = new ArrayList<CarParkInfo>();
	/**
	 * 添加车辆入住登记信息
	 * @return
	 */
	public String addCarParkInfo(){
		String userName = getRandomString(6);
		if (null != carParkInfo) {
			carParkInfo.setUserName(userName);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			String inTime = sdf.format(new Date());
			carParkInfo.setInTime(inTime);
			System.out.println(userName+"\t"+inTime);
		}
		if (null != carParkInfo && (carParkInfoDaoImp.add(carParkInfo))) {
			para = "1";
			ParkInfoSet parkInfoSet = parkInfoSetDaoImp.queryCarNumberByNameAndType(carParkInfo.getParkName(), carParkInfo.getType());
			String s = parkInfoSet.getUserInfo();
			if (StringUtils.isEmpty(s)) {
				s = carParkInfo.getLocationNo() +",";
			} else {
				s = s + carParkInfo.getLocationNo() +",";
			}
			parkInfoSetDaoImp.modfiy(s, carParkInfo.getParkName());
		} else {
			para = "0";
		}
		return SUCCESS;
	}
	
	/**
	 * 使用预约的车号信息
	 */
	public void getReservationInfo(){
		PrintWriter printWriter = null; 
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			List<Reservation> lists = reservationDaoImp.queryReservationByUserName(name);
			if (null != lists && lists.size() == 0) {
				Map map = new HashMap<String,String>();
				map.put("isOk", "noreservationinfo");
				printWriter.write(new Gson().toJson(map));
				printWriter.flush();
			} else {
				Reservation r = lists.get(0);
				CarParkInfo carParkInfo = new CarParkInfo();
				carParkInfo.setCarNo(carNo);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
				String inTime = sdf.format(new Date());
				carParkInfo.setInTime(inTime);
				carParkInfo.setLocationNo(r.getParkNo());
				carParkInfo.setType(Integer.parseInt(r.getType()));
				carParkInfo.setParkName(r.getParkName());
				carParkInfo.setUserName(r.getUserName());
				carParkInfoDaoImp.add(carParkInfo);
				Map map = new HashMap<String,String>();
				map.put("isOk", "success");
				printWriter.write(new Gson().toJson(map));
				printWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally{
			printWriter.close();
		}
	}
	
	
	public String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }    
	/**
	 * 查询所有
	 * @return
	 */
	public String queryAllCarInfo(){
		listData = carParkInfoDaoImp.queryAll();
		List<ParkInfoSet> parkInfoSets = parkInfoSetDaoImp.queryAll();
		for (ParkInfoSet parkInfoSet : parkInfoSets) {
			parkList.add(parkInfoSet.getParkName());
		}
		
		return SUCCESS;
	}
	
	public void selectCarParkInfoByName(){
		ParkInfoSet parkInfoSet = parkInfoSetDaoImp.queryParkInfoByName(name);
		if (null != parkInfoSet) {
			PrintWriter printWriter = null; 
			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				printWriter = response.getWriter();
				Map map = new HashMap<String,String>();
				String saleOrRent = parkInfoSet.getUserInfo();
				Integer count = parkInfoSet.getParkNumber();
				for (int i = 1; i <= count; i++) {
					if (null ==saleOrRent) {
						locationList.add(i+"");
					} else if (!saleOrRent.contains(i+",")) {
						locationList.add(i+"");
					}
				}
				map.put("locationList", locationList);
				printWriter.write(new Gson().toJson(map));
				printWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
				
			} finally{
				printWriter.close();
			}
		}
	}
	/**
	 * 查询信息
	 * @return
	 */
	public void queryCarNumberByNameAndType(){
		parkInfoSet = new ParkInfoSetDaoImp().queryCarNumberByNameAndType(name,Integer.parseInt(type));
		PrintWriter printWriter = null; 
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			Map map = new HashMap<String,String>();
			if (null != parkInfoSet) {
				map.put("count", parkInfoSet.getParkNumber());
				map.put("userInfo", null == parkInfoSet.getUserInfo() ? "":parkInfoSet.getUserInfo());
			} else {
				map.put("count", 0);
				map.put("userInfo", "");
			}
			printWriter.write(new Gson().toJson(map));
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally{
			printWriter.close();
		}
	}
	/**
	 * 删除信息
	 * @return
	 */
	public String deleteCarInfo(){
		if (null != id){
			CarParkInfo carParkInfo = new CarParkInfo();
			carParkInfo.setId(Integer.parseInt(id));
			if(carParkInfoDaoImp.delete(carParkInfo)){
				para="5";
			} else {
				para="6";
			}
		}
		System.out.println("para======"+para);
		return SUCCESS;
	}
	
	/**
	 * 修改信息
	 */
	public void modifyCarParkInfo(){
		Dumper.dump(id);
		CarParkInfo carParkInfo = carParkInfoDaoImp.queryCarParkById(Integer.parseInt(id));
		Map map = new HashMap<String,String>();
		map.put("userName", carParkInfo.getUserName());
		map.put("parkName", carParkInfo.getParkName());
		map.put("carNo", carParkInfo.getCarNo());
		map.put("inTime", carParkInfo.getInTime());
		map.put("type", carParkInfo.getType());
		map.put("outTime", carParkInfo.getOutTime());
		map.put("locationNo", carParkInfo.getLocationNo());
		Gson gson = new Gson();
		String date = gson.toJson(map);
		Dumper.dump(date);
		PrintWriter printWriter = null; 
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			printWriter.write(date);
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally{
			printWriter.close();
		}
	}
	
	/**
	 * @return
	 * @throws ParseException 
	 */
	public void addChargeInfo() throws ParseException{
		//CarParkInfo carParkInfo = carParkInfoDaoImp.queryCarParkBByName(name);
		CarParkInfo carParkInfo = carParkInfoDaoImp.queryCarParkBByCarNo(carNo);
		ParkInfoSet parkInfoSet = new ParkInfoSetDaoImp().queryParkInfoByName(carParkInfo.getParkName());
		Dumper.dump(parkInfoSet);
		Map map = new HashMap<String,String>();
		map.put("userName", carParkInfo.getUserName());
		map.put("parkName", carParkInfo.getParkName());
		map.put("carNo", carParkInfo.getCarNo());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		long millionSeconds = sdf.parse(carParkInfo.getInTime()).getTime();//����
		long currentTime = System.currentTimeMillis();
		int hour = (int)((currentTime - millionSeconds)/3600000 == 0 ? (currentTime - millionSeconds)/3600000 : (currentTime - millionSeconds)/3600000+1);
		map.put("hour", hour);
		map.put("type", carParkInfo.getType());
		map.put("locationNo", carParkInfo.getLocationNo());
		map.put("charge", parkInfoSet.getFeeScale()*hour);
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.setCarNo(carParkInfo.getCarNo());
		chargeInfo.setCharge(parkInfoSet.getFeeScale()*hour);
		chargeInfo.setCreateTime(MyDate.getMyDate_2());
		chargeInfo.setHour(hour);
		chargeInfo.setLocationNo(Integer.parseInt(carParkInfo.getLocationNo()));
		chargeInfo.setParkName(carParkInfo.getParkName());
		chargeInfo.setType(carParkInfo.getType());
		chargeInfo.setUserName(carParkInfo.getUserName());
		chargeInfo.setIsCharge(1);
		new ChargeInfoDaoImp().add(chargeInfo);
		
		carParkInfo.setOutTime(MyDate.getMyDate_2());
		carParkInfoDaoImp.modfiy(carParkInfo);
		
		Gson gson = new Gson();
		String date = gson.toJson(map);
		/**
		 * 结完账，把车位改为已空闲
		 */
		String userInfo = parkInfoSet.getUserInfo().replace(carParkInfo.getLocationNo()+",", "");
		parkInfoSetDaoImp.modfiy(userInfo, parkInfoSet.getParkName());
		PrintWriter printWriter = null; 
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			printWriter.write(date);
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			printWriter.close();
		}
	}
	
	public void addCarNumberByNameAndType(){
		PrintWriter printWriter = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userName =  null != session.getAttribute(Constants.USER_NAME) ? session.getAttribute(Constants.USER_NAME).toString() : "";
			String userType =  null != session.getAttribute(Constants.TYPE) ? session.getAttribute(Constants.TYPE).toString() : "";
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(userType)) {
				if ("1".equals(userType)) {
					System.out.println("管理员不能执行此操作");
					Map map = new HashMap<String,String>();
					map.put("isOk", "no");
					printWriter.write(new Gson().toJson(map));
					printWriter.flush();
				} else {
					List<Reservation> lists = reservationDaoImp.queryReservationByUserName(userName);
					if (null != lists && lists.size() > 0) {
						Reservation r = lists.get(0);
						r.setParkNo(r.getParkNo()+""+count+",");
						r.setParkName(name);
						reservationDaoImp.modfiy(r);
					} else {
						Reservation reservation =  new Reservation();
						reservation.setParkName(name);
						reservation.setParkNo(count+",");
						reservation.setUserName(userName);
						reservation.setType(type);
						reservationDaoImp.add(reservation);
					}
					ParkInfoSet parkInfoSet = parkInfoSetDaoImp.queryCarNumberByNameAndType(name, Integer.parseInt(type));
					String s = parkInfoSet.getUserInfo();
					if (StringUtils.isEmpty(s)) {
						s = count +",";
					} else {
						s = s + count +",";
					}
					parkInfoSetDaoImp.modfiy(s, name);
					Map map = new HashMap<String,String>();
					map.put("isOk", "yes");
					printWriter.write(new Gson().toJson(map));
					printWriter.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			printWriter.close();
		}
	}
	
	public void removeCarNumberByNameAndType(){
		PrintWriter printWriter = null; 
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userName =  null != session.getAttribute(Constants.USER_NAME) ? session.getAttribute(Constants.USER_NAME).toString() : "";
			List<Reservation> lists = reservationDaoImp.queryReservationByUserName(userName);
			if (lists.size() == 0 || !(lists.get(0).getParkName().equals(name) && lists.get(0).getParkNo().contains(count+","))) {
				Map map = new HashMap<String,String>();
				map.put("isOk", "no_permission");
				printWriter.write(new Gson().toJson(map));
				printWriter.flush();
			} else {
				Reservation reservation = lists.get(0);
				String resString = reservation.getParkNo();
				if (!resString.isEmpty() ){
					reservation.setParkNo(resString.replace(count+",", ""));
				}
				//reservationDaoImp.delete(reservation);
				reservationDaoImp.modfiy(reservation);
				ParkInfoSet parkInfoSet = parkInfoSetDaoImp.queryCarNumberByNameAndType(name, Integer.parseInt(type));
				String s = parkInfoSet.getUserInfo();
				s = s.replace(count+",", "");
				parkInfoSetDaoImp.modfiy(s, name);
				Map map = new HashMap<String,String>();
				map.put("isOk", "yes_permission");
				printWriter.write(new Gson().toJson(map));
				printWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			printWriter.close();
		}
	}
	public CarParkInfo getCarParkInfo() {
		return carParkInfo;
	}

	public void setCarParkInfo(CarParkInfo carParkInfo) {
		this.carParkInfo = carParkInfo;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public List<CarParkInfo> getListData() {
		return listData;
	}

	public void setListData(List<CarParkInfo> listData) {
		this.listData = listData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ParkInfoSet getParkInfoSet() {
		return parkInfoSet;
	}
	public void setParkInfoSet(ParkInfoSet parkInfoSet) {
		this.parkInfoSet = parkInfoSet;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public List<String> getParkList() {
		return parkList;
	}

	public void setParkList(List<String> parkList) {
		this.parkList = parkList;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}
	
	
}

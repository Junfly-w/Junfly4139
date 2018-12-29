package park.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import park.daoImp.ParkInfoSetDaoImp;
import park.daoImp.UserDaoImp;
import park.entity.ParkInfoSet;
import park.entity.User;
import park.util.Constants;
import park.util.Dumper;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

public class ParkInfoAction extends ActionSupport {
	private ParkInfoSet parkInfoSet;
	private UserDaoImp userDaoImp = new UserDaoImp();
	private ParkInfoSetDaoImp parkInfoSetDaoImp = new ParkInfoSetDaoImp();
	private String errorInfo;
	private String id;
	private String para;
	private List<ParkInfoSet> listPark = new ArrayList<ParkInfoSet>();
	private List<String> listData = new ArrayList<String>();
	public String addParkInfo(){
		System.out.println(parkInfoSet);
		if (null == parkInfoSet) {
			listPark = parkInfoSetDaoImp.queryAll();
		} else {
			if (null !=parkInfoSet.getId() && parkInfoSet.getId() > 0) {
				parkInfoSetDaoImp.modfiy(parkInfoSet);
			} else {
				parkInfoSetDaoImp.add(parkInfoSet);
			}
			listPark = parkInfoSetDaoImp.queryAll();
		}
		return SUCCESS;
	}
	
	public String getListParkInfo(){
		listPark = parkInfoSetDaoImp.queryAll();
		return SUCCESS;
	}
	public String queryParkInfo(){
		String userName = (String) ServletActionContext.getRequest().getSession().getAttribute(Constants.USER_NAME);
		User user = userDaoImp.queryUserByName(userName);
		if (null == user ){
			return "input";
		}
		if (user.getType() == 0) {
			errorInfo = "你无权限做此操作";
		} else if (parkInfoSetDaoImp.add(parkInfoSet)) {
			listPark = parkInfoSetDaoImp.queryAll();
		}
		return SUCCESS;
	}
	
	public String deleteParkInfo(){
		ParkInfoSet parkInfoSet = new ParkInfoSet();
		parkInfoSet.setId(Integer.parseInt(id));
		if (parkInfoSetDaoImp.delete(parkInfoSet)) {
			para="5";
		} else {
			System.out.println("======删除失败=====");
			para="6";
		}
		return SUCCESS;
	}
	public String getAllParkName(){
		List<ParkInfoSet> listPark = parkInfoSetDaoImp.queryAll();
		for (ParkInfoSet parkInfoSet : listPark) {
			listData.add(parkInfoSet.getParkName());
		}
		Dumper.dump(listData);
		return SUCCESS;
	}
	
	public String queryBespeakInfo(){
		List<ParkInfoSet> listPark = parkInfoSetDaoImp.queryAll();
		for (ParkInfoSet parkInfoSet : listPark) {
			listData.add(parkInfoSet.getParkName());
		}
		Dumper.dump(listData);
		return SUCCESS;
	}
	public void modifyParkInfo(){
		Dumper.dump(id);
		ParkInfoSet parkInfoSet = parkInfoSetDaoImp.queryParkInfoById(Integer.parseInt(id));
		Map map = new HashMap<String,String>();
		map.put("id", parkInfoSet.getId());
		map.put("parkName", parkInfoSet.getParkName());
		map.put("parkLocation", parkInfoSet.getParkLocation());
		map.put("openDate", parkInfoSet.getOpenDate());
		map.put("closeDate", parkInfoSet.getCloseDate());
		map.put("parkNumber", parkInfoSet.getParkNumber());
		map.put("rentOrSale", parkInfoSet.getRentOrSale());
		map.put("feeScale", parkInfoSet.getFeeScale());
		map.put("userInfo", parkInfoSet.getUserInfo());
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
	public ParkInfoSet getParkInfoSet() {
		return parkInfoSet;
	}

	public void setParkInfoSet(ParkInfoSet parkInfoSet) {
		this.parkInfoSet = parkInfoSet;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public List<ParkInfoSet> getListPark() {
		return listPark;
	}
	public void setListPark(List<ParkInfoSet> listPark) {
		this.listPark = listPark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public List<String> getListData() {
		return listData;
	}

	public void setListData(List<String> listData) {
		this.listData = listData;
	}
	
	
	
}

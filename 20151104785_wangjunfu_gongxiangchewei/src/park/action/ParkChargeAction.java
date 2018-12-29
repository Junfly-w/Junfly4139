package park.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import park.daoImp.ChargeInfoDaoImp;
import park.daoImp.UserDaoImp;
import park.entity.ChargeInfo;
import park.entity.User;
import park.util.Constants;
import park.util.Dumper;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

public class ParkChargeAction extends ActionSupport{
	private List<ChargeInfo> list = new ArrayList<ChargeInfo>();
	private ChargeInfoDaoImp chargeInfoDaoImp = new ChargeInfoDaoImp();
	private UserDaoImp userDaoImp = new UserDaoImp();
	private String id;
	private String userName;
	public String queryAllChargeInfo(){
		System.out.println("========");
		list = chargeInfoDaoImp.queryAll();
		return SUCCESS;
	}
	
	public void querUserInfo(){
		List<ChargeInfo> chargeInfos = null;
		if (StringUtils.isEmpty(userName)) {
			userName = (String) ServletActionContext.getRequest().getSession().getAttribute(Constants.USER_NAME);
			User user = userDaoImp.queryUserByName(userName);
			if (1==user.getType()){
				chargeInfos = chargeInfoDaoImp.queryAll();
			}else{
				chargeInfos = chargeInfoDaoImp.queryCarParkBByName(userName);
			}
		} else {
			chargeInfos = chargeInfoDaoImp.queryCarParkBByName(userName);
		}
		List<Object> datas = new ArrayList<>();
		for (ChargeInfo chargeInfo : chargeInfos) {
			Map<String, String> data = new HashMap<String,String>();
			data.put("userName", chargeInfo.getUserName());
			data.put("parkName", chargeInfo.getParkName());
			data.put("carNo", chargeInfo.getCarNo());
			data.put("hour", chargeInfo.getHour()+"");
			if (chargeInfo.getType() == 0) {
				data.put("type", "租");
			} else {
				data.put("type", "售");
			}
			data.put("charge", chargeInfo.getCharge()+"");
			data.put("createTime", chargeInfo.getCreateTime());
			data.put("id", chargeInfo.getId()+"");
			datas.add(data);
		}
		Dumper.dump(datas);
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter printWriter = null;
		try {
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			printWriter.write(new Gson().toJson(datas));
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			printWriter.close();
		}
	}
	public String deleteChargeInfo(){
		chargeInfoDaoImp.delete(Integer.parseInt(id));
		list = chargeInfoDaoImp.queryAll();
		return SUCCESS;
	}
	
	public void deleteUserChargeInfo(){
		ChargeInfo chargeInfo = chargeInfoDaoImp.queryCarParkById(Integer.parseInt(id));
		chargeInfoDaoImp.delete(Integer.parseInt(id));
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter printWriter = null;
		try {
			response.setCharacterEncoding("utf-8");
			printWriter = response.getWriter();
			printWriter.write(new Gson().toJson(null == chargeInfo?"":chargeInfo.getUserName()));
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			printWriter.close();
		}
	}
	public List<ChargeInfo> getList() {
		return list;
	}
	public void setList(List<ChargeInfo> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}

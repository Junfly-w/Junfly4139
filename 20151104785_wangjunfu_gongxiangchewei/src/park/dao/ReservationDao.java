package park.dao;

import java.util.List;

import park.entity.Reservation;


public interface ReservationDao {
	//修改用户
	public boolean modfiy(Reservation reservation);
	//添加用户
	public boolean add(Reservation reservation);
	//删除用户
	public boolean delete(Reservation reservation);
	//查询所有的用户
	public List<Reservation> queryAll();
	
	public List<Reservation> queryReservationByUserNameAndType(String userName,String type);

}

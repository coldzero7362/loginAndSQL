package my.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
	
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {//다른 곳에서도 이 클래스에 접속하게 해줌
		return instance; // 인스턴스를 반환함
	}
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr","hr");
		} catch (ClassNotFoundException e) {
			System.out.println("driver not found");
		} catch (SQLException e) {
			System.out.println("DB connection failed");
		}
		return conn;
	}
	
	public List<MemberDTO> selectMembers() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		try {
			conn = getConnection();
			
			String sql = "select * from member";
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();
			list = makeArray(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		return list;
	}
	
	private List<MemberDTO> makeArray(ResultSet rs) throws SQLException {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		while(rs.next()) {
			MemberDTO dto = new MemberDTO();
			dto.setName(rs.getString("name"));
			dto.setId(rs.getString("id"));
			dto.setPass(rs.getString("pass"));
			dto.setBirth(rs.getInt("birth"));
			dto.setGender(rs.getString("gender"));
			dto.setJob(rs.getString("job"));
			dto.setAddress(rs.getString("address"));
			dto.setRegDate(rs.getDate("regDate"));
			list.add(dto);
		}
		return list;
	}
	
	
	public boolean idAvailableChk(String id) {
		boolean result = false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			String sql = "select * from member where id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			if(!rs.next()) result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		return result;
	}
	
	public boolean insertMember(MemberDTO dto) {
		boolean result = false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			System.out.println(dto.getName());
			String sql = "insert into member values(?,?,?,?,?,?,?,sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getName());
			ps.setString(2, dto.getId());
			ps.setString(3, dto.getPass());
			ps.setInt(4, dto.getBirth());
			ps.setString(5, dto.getGender());
			ps.setString(6, dto.getJob());
			ps.setString(7, dto.getAddress());
			
			int n = ps.executeUpdate();
			if(n>0) result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		return result;
		
	}
	
	public int loginCheck(String id, String pass) {
		int result = -1; // -1: not found  0: pwd error  1: success
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			
			String sql = "select pass from member where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				String dbPass = rs.getString("pass");
				if (dbPass.equals(pass)) {
					result = 1;
				} else {
					result = 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		return result;
	}
	
	public MemberDTO selectMember(String id) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		MemberDTO dto = new MemberDTO();
		try {
			conn = getConnection();
			String sql = "select * from member where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				dto.setName(rs.getString(1));
				dto.setId(rs.getString(2));
				dto.setPass(rs.getString(3));
				dto.setBirth(rs.getInt(4));
				dto.setGender(rs.getString(5));
				dto.setJob(rs.getString(6));
				dto.setAddress(rs.getString(7));
				dto.setRegDate(rs.getDate(8));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		
		return dto;
	}
	
	public boolean updateMember(MemberDTO dto,String id) {
		int n = 0;
		boolean result=false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			String sql = "UPDATE member SET pass=?,birth=?,gender=?,job=?,address=? WHERE id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getPass());
			ps.setInt(2, dto.getBirth());
			ps.setString(3, dto.getGender());
			ps.setString(4, dto.getJob());
			ps.setString(5, dto.getAddress());
			ps.setString(6, id);
			
			n = ps.executeUpdate();
			
			if(n>0) result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { if(ps!=null) ps.close();} catch(SQLException e) { }
			try { if(rs!=null) rs.close();} catch(SQLException e) { }
			try { if(conn!=null) conn.close();} catch(SQLException e) { }
		}
		
		return result;
	}
	
	public boolean deleteMember(String id,String pass) {
		boolean result=false;
		int login = loginCheck(id, pass);
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			if(login == 1) {
				conn = getConnection();
				String sql = "DELETE FROM member WHERE id=?";
				ps = conn.prepareStatement(sql);
				
				ps.setString(1, id);
				System.out.println(id);
				int n = ps.executeUpdate();
				if(n>0) {
					result = true;
				}
				
			}else {
				result = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}
		return result;
		
	}
	

}

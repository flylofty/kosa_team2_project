package kr.co.enjo2.dao.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import kr.co.enjo2.dto.member.MemberDto;

public class MemberDao {

	DataSource ds = null;

	public MemberDao() throws NamingException {
		Context context = new InitialContext();
		ds = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
	}

	// 회원 단건 조회
	public MemberDto findOne(String id) {
		MemberDto member = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			String sql = "select mem_id, mem_nic, mem_pwd, mem_email, mem_type " 
					   + "from member "
					   + "where mem_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				member = new MemberDto();
				member.setId(rs.getString("mem_id"));
				member.setNickName(rs.getString("mem_nic"));
				member.setPassword(rs.getString("mem_pwd"));
				member.setEmail(rs.getString("mem_email"));
				member.setMemberType(rs.getString("mem_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			try {
				pstmt.close();
				rs.close();
				conn.close();
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
		return member;
	}
	
	// 회원 저장하기(회원가입)
	public int saveOne(MemberDto member) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int row = 0;
		
		try {
			conn = ds.getConnection();
			String sql="insert into member(mem_id, mem_nic, mem_pwd, mem_email, mem_type) "+
			           "values(?, ?, ?, ?, ?)";
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getNickName());
			pstmt.setString(3, member.getPassword());
			pstmt.setString(4, member.getEmail());
			// 나중에 바뀔 수 있음
			pstmt.setString(5, "user");
			row = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
		
		return row;
	}
}

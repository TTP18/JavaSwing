package DAO;


import DTO.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
public class DAOPhieuNhap {
	
	private Connection con;
	
	public boolean openConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String dbUrl = "jdbc:sqlserver://LAPTOP-4DA46HIV:1433;DatabaseName=QuanLyNhaThuoc;encrypt = false;";
			String username = "sa"; String password= "123";
			con = DriverManager.getConnection(dbUrl, username, password);
			return true;
		} catch (Exception ex) {
			System.out.println(ex); 
			return false; }
	}
	
	public void closeConnection() {
		try {
			if (con!=null)
				con.close();
		} catch (SQLException ex) {
			System.out.println(ex);}
	}
	
	public ArrayList<PhieuNhap> getListPhieuNhap(){
		
		 ArrayList<PhieuNhap> listPhieuNhap = new ArrayList<>();
		 
		 if (openConnection()) {
			 try {
				 String sql = "Select * from tblPhieuNhap";
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(sql);
				 while(rs.next()){
					 PhieuNhap phieuNhap = new PhieuNhap();
					 phieuNhap.setMaPhieuNhap(rs.getString(1));
					 phieuNhap.setMaNCC(rs.getString(2));
					 phieuNhap.setSoLuong(rs.getInt(3));
					 phieuNhap.setNgayLap(rs.getDate(4));
					 phieuNhap.setTongTien(rs.getFloat(5));
					 listPhieuNhap.add(phieuNhap);
				 }
				 
			 }catch (SQLException ex) {
				 System.out.println(ex);
			 }
		 }else { 
			 closeConnection();
		 } 
			 return listPhieuNhap;
	}
	
	public String generateNewPN() {
        String newPN = null;
        

        if (openConnection()) {
            try {
                // Truy vấn để lấy mã phiếu nhập cuối cùng
                String sql = "SELECT MAX(MaPhieuNhap) AS LastPN FROM tblPhieuNhap";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    String lastPN = rs.getString("LastPN");
                    if (lastPN != null) {
                        // Lấy số sau chữ "PN" và tăng lên 1 để tạo mã mới
                        int lastNumber = Integer.parseInt(lastPN.substring(2));
                        newPN = "PN" + String.format("%02d", lastNumber + 1); // Format lại để có định dạng PN01, PN02,...
                    } else {
                        // Nếu chưa có phiếu nhập nào trong CSDL, bắt đầu với PN01
                        newPN = "PN01";
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            } finally {
                closeConnection();
            }
        }

        return newPN;
    }
	
	public boolean addPhieuNhap(PhieuNhap phieuNhap) {
        boolean result = false;

        // Lấy mã phiếu nhập mới
        String newPN = generateNewPN();

        if (newPN != null && openConnection()) {
            try {
                // Thêm phiếu nhập mới vào CSDL với mã phiếu nhập mới
                String sql = "INSERT INTO tblPhieuNhap (MaPhieuNhap, MaNCC, SoLuong, NgayLap, TongTien) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, newPN); // Thiết lập mã phiếu nhập mới
                stmt.setString(2, phieuNhap.getMaNCC());
                stmt.setInt(3, phieuNhap.getSoLuong());
                stmt.setDate(4, phieuNhap.getNgayLap());
                stmt.setFloat(5, phieuNhap.getTongTien());

                if (stmt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            } finally {
                closeConnection();
            }
        }

        return result;
    }
	
	public boolean checkMaPhieuNhap(String maPhieuNhap){
		
		boolean result = false; 
		if (openConnection()) {
			try { 
				
				String sql = "Select * from tblPhieuNhap where MaPhieuNhap = ?";
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, maPhieuNhap);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
	                result = true; // PhieuNhap với maPhieuNhap khớp tồn tại
	            }
				
			}catch (SQLException ex) {
			System.out.println(ex);
			}
		finally {
		closeConnection();}
		}
		return result;

	}

	public boolean deletePhieuNhap(String maPhieuNhap ){
        boolean result = false; 
        if(openConnection())
        {
	        try {
	           String sql = "DELETE FROM tblPhieuNhap WHERE MaPhieuNhap=?";
	           PreparedStatement ps = con.prepareStatement(sql);
	           ps.setString(1,maPhieuNhap);
	           result = ps.executeUpdate() > 0;
	        }catch(SQLException e){
	            e.printStackTrace(); 
	        }
        }
        
        return result;
    }

	public ArrayList<PhieuNhap> findPhieuNhap(String searchKeyword) {
        ArrayList<PhieuNhap> result = new ArrayList<>();

        if (openConnection()) {
            try {
                String sql = "SELECT MaPhieuNhap,tblNCC.MaNCC,SoLuong,NgayLap,TongTien FROM tblPhieuNhap,tblNCC WHERE tblPhieuNhap.MaNCC = tblNCC.MaNCC and (MaPhieuNhap = ? OR TenNCC LIKE ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, searchKeyword);
                stmt.setString(2, "%" + searchKeyword + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String maPhieuNhap = rs.getString("MaPhieuNhap");
                    String maNCC = rs.getString("MaNCC");
                    int soluong = rs.getInt("SoLuong");
                    Date ngay = rs.getDate("NgayLap");
                    float tongTien = rs.getFloat("TongTien");
                    PhieuNhap phieuNhap = new PhieuNhap();
                    phieuNhap.setMaPhieuNhap(maPhieuNhap);
                    phieuNhap.setMaNCC(maNCC);
                    phieuNhap.setSoLuong(soluong);
                    phieuNhap.setNgayLap(ngay);
                    phieuNhap.setTongTien(tongTien);
                    result.add(phieuNhap);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            } finally {
                closeConnection();
            }
        }

        return result;
    }
	
	public boolean updatePhieuNhap(PhieuNhap phieuNhap) {
		boolean result = false;

		if (openConnection()) {
			try {
				String sql = "UPDATE tblPhieuNhap SET MaNCC = ?, soLuong = ?, ngayLap = ?, tongTien = ? WHERE maPhieuNhap = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, phieuNhap.getMaNCC());
	            ps.setInt(2, phieuNhap.getSoLuong());
	            ps.setDate(3, phieuNhap.getNgayLap());
	            ps.setDouble(4, phieuNhap.getTongTien());
	            ps.setString(5, phieuNhap.getMaPhieuNhap());
	            if (ps.executeUpdate() > 0) {
	                result = true;
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}
		return result;
	}
	
	
}

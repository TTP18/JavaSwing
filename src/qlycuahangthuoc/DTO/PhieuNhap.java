package DTO;

import java.sql.Date;
import java.time.LocalDate;

public class PhieuNhap {
	
	private String maPhieuNhap;
	private String maNCC;
	private int soLuong;
	private Date ngayLap;
	private float tongTien;
	
	public PhieuNhap()
	{		
	}
	
	public PhieuNhap(String maPhieuNhap, String maNCC, int soLuong, Date ngayLap, float tongTien) {
		this.maPhieuNhap = maPhieuNhap;
		this.maNCC = maNCC;
		this.soLuong = soLuong;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
	}

	public String getMaPhieuNhap() {
		return maPhieuNhap;
	}

	public void setMaPhieuNhap(String maPhieuNhap) {
		this.maPhieuNhap = maPhieuNhap;
	}

	public String getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public Date getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(Date currentDate) {
		this.ngayLap = currentDate;
	}

	public float getTongTien() {
		return tongTien;
	}

	public void setTongTien(float tongTien) {
		this.tongTien = tongTien;
	}

	public void setNgayLap(String string) {
		// TODO Auto-generated method stub
		
	}
	
	

}

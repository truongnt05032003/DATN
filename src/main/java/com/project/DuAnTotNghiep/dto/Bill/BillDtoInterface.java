package com.project.DuAnTotNghiep.dto.Bill;

import java.time.LocalDateTime;

import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;

public interface BillDtoInterface {
    Long getMaHoaDon();
    String getMaDinhDanh();
    String getHoVaTen();
    String getSoDienThoai();
    Double getTienKhuyenMai();
    LocalDateTime getNgayTao();
    Double getTongTien();
    BillStatus getTrangThai();
    InvoiceType getLoaiDon();
    String getHinhThucThanhToan();

    String getMaGiaoDich();
    String getMaDoiTra();
}

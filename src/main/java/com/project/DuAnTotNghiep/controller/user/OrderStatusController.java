package com.project.DuAnTotNghiep.controller.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.DuAnTotNghiep.dto.Cart.CartDto;
import com.project.DuAnTotNghiep.entity.Bill;
import com.project.DuAnTotNghiep.service.BillService;
import com.project.DuAnTotNghiep.service.CartService;

@Controller
public class OrderStatusController {
    private final BillService billService;  // Quản lý các hoạt động liên quan đến hóa đơn
    private final CartService cartService;  // Quản lý các hoạt động liên quan đến giỏ hàng

    public OrderStatusController(BillService billService, CartService cartService) {
        this.billService = billService;
        this.cartService = cartService;
    }

    @GetMapping("/cart-status")  // Hiển thị trạng thái giỏ hàng của người dùng
    public String viewCartStatus(Model model,  // Model Là 1 đối tượng của Spring MVC dùng để truyền dữ liệu từ Controller sang View (html)
                                 @RequestParam(required = false) String status,  // Lấy giá trị tham số status từ URL, required = false tham số này không bắt buộc. Nếu ko đc cung cấp trong URL, giá trị của status sẽ là null
                                 @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC)  Pageable pageable) {  // size = 5 phân trang(mỗi trang tối đa 5 hóa đơn), sort = "createDate" sắp xếp theo trường createdDate(ngày tạo hóa đơn), direction = Sort.Direction.DESC hóa đơn mới sẽ hiển thị trc
        Page<Bill> billPage = null;  // Lưu danh sách hóa đơn phân trang (đc trả về từ service)
        if(status == null || status.trim().isEmpty()) {
            billPage = billService.getBillByAccount(pageable); // Nếu status = null hoặc rỗng thì trả về danh sách hóa đơn của tài khoan người dùng hiện tại, đc phân trang theo đối tượng pageable
        }else {
            billPage = billService.getBillByStatus(status, pageable);
            model.addAttribute("status", status);  // Nếu status đc cung cấp thì gọi phương thức billService.getBillByStatus(status, pageable) để lấy danh sách hóa đơn có trạng thái tương ứng. Thêm status vào model để truyền trạng thái hiện tại sang view
        }

        model.addAttribute("bills", billPage);
        return "user/cart-status";  // Hiển thị trạng thái giỏ hàng đến FE user/cart-status
    }

    @PostMapping("/cancel-bill/{id}")
    public String cancelBill(@PathVariable Long id) {  // @PathVariable ánh xạ giá trị {id} trong URL vào tham số id của phương thức
        billService.updateStatus("HUY", id);
        return "redirect:/cart-status";  // Sau khi hủy hóa đơn khách hàng đc trả về trang trạng thái giỏ hàng cart-status
    }

    @ResponseBody
    @GetMapping("/api/getAllCart")
    public List<CartDto> getAllCart() {
        return cartService.getAllCartByAccountId();
    }

}

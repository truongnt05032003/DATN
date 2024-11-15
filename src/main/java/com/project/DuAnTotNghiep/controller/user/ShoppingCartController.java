package com.project.DuAnTotNghiep.controller.user;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.DuAnTotNghiep.dto.AddressShipping.AddressShippingDto;
import com.project.DuAnTotNghiep.dto.Cart.CartDto;
import com.project.DuAnTotNghiep.dto.DiscountCode.DiscountCodeDto;
import com.project.DuAnTotNghiep.exception.NotFoundException;
import com.project.DuAnTotNghiep.service.AddressShippingService;
import com.project.DuAnTotNghiep.service.BillService;
import com.project.DuAnTotNghiep.service.CartService;
import com.project.DuAnTotNghiep.service.DiscountCodeService;

@Controller
public class ShoppingCartController {
    private final CartService cartService;  // Quản lý các hoạt động liên quan đến giỏ hàng

    private final BillService billService;  // Quản lý các hoạt động liên quan đến hóa đơn

    private final DiscountCodeService discountCodeService;  // Quản lý các mã giảm giá 

    private final AddressShippingService addressShippingService;  //Quản lý địa chỉ giao hàng

    public ShoppingCartController(CartService cartService, BillService billService, DiscountCodeService discountCodeService, AddressShippingService addressShippingService) {
        this.cartService = cartService;
        this.billService = billService;
        this.discountCodeService = discountCodeService;
        this.addressShippingService = addressShippingService;
    }

    @GetMapping("/shoping-cart")  // Xử lý yêu cầu Get để hiển thị giỏ hàng của người dùng
    public String viewShoppingCart(Model model) {
        List<CartDto> cartDtoList = cartService.getAllCartByAccountId();
        Page<DiscountCodeDto> discountCodeList = discountCodeService.getAllAvailableDiscountCode(PageRequest.of(0, 15));
        List<AddressShippingDto> addressShippingDtos = addressShippingService.getAddressShippingByAccountId();
        model.addAttribute("discountCodes", discountCodeList.getContent());
        model.addAttribute("addressShippings", addressShippingDtos);
        model.addAttribute("carts", cartDtoList);
        return "user/shoping-cart";  // Truyền dữ liệu đến FE shoping-cart để hiểu thị
    }

    @ResponseBody  // Cho phép trả về dữ liệu trực tiếp ở dạng JSON
    @PostMapping("/api/addToCart") // Thực hiện hành động thêm vào giỏ hàng trên FE (thêm sp)
    public void addToCart(@RequestBody CartDto cartDto) throws NotFoundException {  //Nếu không tìm thấy sản phẩm, hoặc có lỗi trong quá trình xử lý, một ngoại lệ NotFoundException sẽ được ném ra.
        cartService.addToCart(cartDto);  // @RequestBody ánh xạ dữ liệu requestbody thành đối tượng java (cartDto)
    }  // Client sẽ gửi một JSON object chứa thông tin sản phẩm cần thêm vào giỏ hàng, và nó được chuyển đổi thành một đối tượng CartDto

    @ResponseBody
    @PostMapping("/api/deleteCart/{id}")  // Xóa sản phẩm theo id
    public void deleteCart(@PathVariable Long id) {  // @PathVariable ánh xạ giá trị từ URL tới tham số trong phương thức(id)
        cartService.deleteCart(id);
    }

    @ResponseBody
    @PostMapping("/api/updateCart")  // Update sp
    public void updateCart(@RequestBody CartDto cartDto) throws NotFoundException {
        cartService.updateCart(cartDto);
    }

}


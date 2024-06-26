package com.asm.controller.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asm.entity.Orders;
import com.asm.entity.Products;
import com.asm.entity.dao.OrdersDAO;
import com.asm.entity.dao.ProductsDAO;
import com.asm.services.OrderService;

@Controller
@RequestMapping("app/admin")
public class AdminController {

	@Autowired
	OrdersDAO ordersDAO;

	@Autowired
	ProductsDAO productDAO;
	@Autowired
	OrderService orderService;

	@RequestMapping("/home")
	public String home(Model model) {
		BigDecimal totalTodayAmount = ordersDAO.sumTotalAmountByDate();
		model.addAttribute("totalTodayAmount", totalTodayAmount != null ? totalTodayAmount : BigDecimal.ZERO);

		long pendingOrdersCount = ordersDAO.countByOrderStatus("Pending");
		model.addAttribute("pendingOrdersCount", pendingOrdersCount);

		List<Orders> orders = ordersDAO.findAll();
		model.addAttribute("orders", orders);

		List<Products> products = productDAO.findAll();
		model.addAttribute("products", products);

		BigDecimal sumTotalRevenue = ordersDAO.sumTotalRevenue();
		model.addAttribute("sumTotalRevenue", sumTotalRevenue);
		
		Double sumDailyAmount = orderService.getTotalRevenueForToday(); // Lấy tổng doanh thu của ngày hiện tại
		System.out.println(sumDailyAmount);
		if (sumDailyAmount == null) {
			sumDailyAmount = 0.0;
			return "admin/home";
		} else {
			model.addAttribute("sumDailyAmount", sumDailyAmount);
		}
		
		List<Map<String, Object>> top10Products = orderService.getTop10MostPurchasedProducts();
	    model.addAttribute("top10Products", top10Products);
		return "admin/home";

	}

}